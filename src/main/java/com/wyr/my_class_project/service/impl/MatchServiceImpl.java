package com.wyr.my_class_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.entity.MatchRecord;
import com.wyr.my_class_project.entity.Notification;
import com.wyr.my_class_project.mapper.FoundItemMapper;
import com.wyr.my_class_project.mapper.LostItemMapper;
import com.wyr.my_class_project.mapper.MatchRecordMapper;
import com.wyr.my_class_project.service.MatchService;
import com.wyr.my_class_project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRecordMapper matchRecordMapper;

    @Autowired
    private LostItemMapper lostItemMapper;

    @Autowired
    private FoundItemMapper foundItemMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public void executeMatch() {
        // 获取所有失物（包括待匹配和待认领状态）
        LambdaQueryWrapper<LostItem> lostWrapper = new LambdaQueryWrapper<>();
        lostWrapper.in(LostItem::getStatus, Constants.ITEM_STATUS_PENDING, Constants.ITEM_STATUS_MATCHED);
        List<LostItem> lostItems = lostItemMapper.selectList(lostWrapper);

        for (LostItem lostItem : lostItems) {
            // 获取所有拾物（包括待匹配和待认领状态）
            LambdaQueryWrapper<FoundItem> foundWrapper = new LambdaQueryWrapper<>();
            foundWrapper.in(FoundItem::getStatus, Constants.ITEM_STATUS_PENDING, Constants.ITEM_STATUS_MATCHED);
            List<FoundItem> foundItems = foundItemMapper.selectList(foundWrapper);

            // 删除该失物的旧匹配记录
            matchRecordMapper.delete(
                    new LambdaQueryWrapper<MatchRecord>()
                            .eq(MatchRecord::getLostItemId, lostItem.getId())
                            .eq(MatchRecord::getStatus, 0)
            );

            List<MatchRecord> matches = new ArrayList<>();

            for (FoundItem foundItem : foundItems) {
                // 计算相似度
                double similarity = calculateSimilarity(lostItem, foundItem);

                // 只要有任意匹配就记录（相似度 > 0）
                if (similarity > 0) {
                    MatchRecord record = new MatchRecord();
                    record.setLostItemId(lostItem.getId());
                    record.setFoundItemId(foundItem.getId());
                    record.setSimilarityScore(similarity);
                    record.setMatchType(getMatchType(lostItem, foundItem));
                    record.setStatus(Constants.MATCH_STATUS_PENDING);

                    matches.add(record);
                }
            }

            // 按相似度排序，取前10个
            matches.sort((a, b) -> Double.compare(b.getSimilarityScore(), a.getSimilarityScore()));
            if (matches.size() > 10) {
                matches = matches.subList(0, 10);
            }

            // 保存匹配记录
            for (MatchRecord match : matches) {
                matchRecordMapper.insert(match);
            }

            // 如果有匹配结果且失物还是待匹配状态，更新为待认领
            if (!matches.isEmpty() && lostItem.getStatus() == Constants.ITEM_STATUS_PENDING) {
                lostItem.setStatus(Constants.ITEM_STATUS_MATCHED);
                lostItemMapper.updateById(lostItem);

                // 发送通知
                sendMatchNotification(matches.get(0), lostItem, foundItemMapper.selectById(matches.get(0).getFoundItemId()));
            }
        }
    }

    @Override
    public List<MatchRecord> getByLostItemId(Long lostItemId) {
        return matchRecordMapper.selectByLostItemId(lostItemId);
    }

    @Override
    public List<MatchRecord> getByFoundItemId(Long foundItemId) {
        return matchRecordMapper.selectByFoundItemId(foundItemId);
    }

    @Override
    @Transactional
    public void confirmMatch(Long matchId, Long userId) {
        MatchRecord match = matchRecordMapper.selectById(matchId);
        if (match == null) {
            throw new BusinessException(ResultCode.MATCH_NOT_FOUND);
        }

        match.setStatus(Constants.MATCH_STATUS_CONFIRMED);
        matchRecordMapper.updateById(match);
    }

    @Override
    @Transactional
    public void rejectMatch(Long matchId, Long userId) {
        MatchRecord match = matchRecordMapper.selectById(matchId);
        if (match == null) {
            throw new BusinessException(ResultCode.MATCH_NOT_FOUND);
        }

        match.setStatus(Constants.MATCH_STATUS_REJECTED);
        matchRecordMapper.updateById(match);
    }

    @Override
    @Transactional
    public void manualMatch(Long lostItemId, Long foundItemId) {
        LostItem lostItem = lostItemMapper.selectById(lostItemId);
        FoundItem foundItem = foundItemMapper.selectById(foundItemId);

        if (lostItem == null || foundItem == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }

        // 计算相似度
        double similarity = calculateSimilarity(lostItem, foundItem);

        // 创建匹配记录
        MatchRecord record = new MatchRecord();
        record.setLostItemId(lostItemId);
        record.setFoundItemId(foundItemId);
        record.setSimilarityScore(similarity);
        record.setMatchType("manual");
        record.setStatus(Constants.MATCH_STATUS_CONFIRMED);  // 管理员手动匹配直接确认

        matchRecordMapper.insert(record);

        // 更新物品状态
        lostItem.setStatus(Constants.ITEM_STATUS_MATCHED);
        lostItemMapper.updateById(lostItem);

        foundItem.setStatus(Constants.ITEM_STATUS_MATCHED);
        foundItemMapper.updateById(foundItem);

        // 发送通知
        sendMatchNotification(record, lostItem, foundItem);
    }

    /**
     * 计算相似度 - 基于名称、描述、类别综合匹配
     */
    private double calculateSimilarity(LostItem lostItem, FoundItem foundItem) {
        double score = 0;

        // 名称相似度 (40分)
        if (isSimilar(lostItem.getTitle(), foundItem.getTitle())) {
            score += 40;
        }

        // 类别匹配 (20分)
        if (lostItem.getCategoryId().equals(foundItem.getCategoryId())) {
            score += 20;
        }

        // 描述相似度 (25分)
        if (isSimilar(lostItem.getDescription(), foundItem.getDescription())) {
            score += 25;
        }

        // 地点相似度 (10分)
        if (isSimilar(lostItem.getLostLocation(), foundItem.getFoundLocation())) {
            score += 10;
        }

        // 时间接近度 (5分)
        if (lostItem.getLostTime() != null && foundItem.getFoundTime() != null) {
            long hoursDiff = Math.abs(ChronoUnit.HOURS.between(lostItem.getLostTime(), foundItem.getFoundTime()));
            if (hoursDiff <= 24) {
                score += 5;
            } else if (hoursDiff <= 72) {
                score += 3;
            }
        }

        return score;
    }

    /**
     * 判断两个字符串是否相似
     */
    private boolean isSimilar(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }

        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        // 包含关系
        if (str1.contains(str2) || str2.contains(str1)) {
            return true;
        }

        // 计算相似度
        int maxLen = Math.max(str1.length(), str2.length());
        if (maxLen == 0) {
            return true;
        }

        int distance = levenshteinDistance(str1, str2);
        double similarity = 1 - (double) distance / maxLen;

        return similarity >= 0.6;
    }

    /**
     * 计算编辑距离
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * 获取匹配类型
     */
    private String getMatchType(LostItem lostItem, FoundItem foundItem) {
        List<String> types = new ArrayList<>();

        if (isSimilar(lostItem.getTitle(), foundItem.getTitle())) {
            types.add("名称");
        }
        if (lostItem.getCategoryId().equals(foundItem.getCategoryId())) {
            types.add("类别");
        }
        if (isSimilar(lostItem.getDescription(), foundItem.getDescription())) {
            types.add("描述");
        }
        if (isSimilar(lostItem.getLostLocation(), foundItem.getFoundLocation())) {
            types.add("地点");
        }

        return String.join("+", types);
    }

    /**
     * 发送匹配通知
     */
    private void sendMatchNotification(MatchRecord match, LostItem lostItem, FoundItem foundItem) {
        // 通知失主
        Notification lostNotification = new Notification();
        lostNotification.setUserId(lostItem.getUserId());
        lostNotification.setTitle("物品匹配成功");
        lostNotification.setContent("您丢失的 \"" + lostItem.getTitle() + "\" 可能已被找到，请查看匹配结果。");
        lostNotification.setType(Constants.NOTIFICATION_TYPE_MATCH);
        lostNotification.setRelatedId(match.getId());
        notificationService.send(lostNotification);

        // 通知拾取者
        Notification foundNotification = new Notification();
        foundNotification.setUserId(foundItem.getUserId());
        foundNotification.setTitle("物品匹配成功");
        foundNotification.setContent("您拾到的 \"" + foundItem.getTitle() + "\" 可能有人认领，请查看匹配结果。");
        foundNotification.setType(Constants.NOTIFICATION_TYPE_MATCH);
        foundNotification.setRelatedId(match.getId());
        notificationService.send(foundNotification);
    }
}
