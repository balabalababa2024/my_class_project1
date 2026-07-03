package com.wyr.my_class_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.entity.MatchRecord;
import com.wyr.my_class_project.entity.ClaimRequest;
import com.wyr.my_class_project.mapper.FoundItemMapper;
import com.wyr.my_class_project.mapper.LostItemMapper;
import com.wyr.my_class_project.mapper.MatchRecordMapper;
import com.wyr.my_class_project.mapper.ClaimRequestMapper;
import com.wyr.my_class_project.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private LostItemMapper lostItemMapper;

    @Autowired
    private FoundItemMapper foundItemMapper;

    @Autowired
    private MatchRecordMapper matchRecordMapper;

    @Autowired
    private ClaimRequestMapper claimRequestMapper;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // 总失物数
        Long totalLost = lostItemMapper.selectCount(null);
        stats.put("totalLost", totalLost);

        // 总拾物数
        Long totalFound = foundItemMapper.selectCount(null);
        stats.put("totalFound", totalFound);

        // 待匹配数
        Long pendingLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>().eq(LostItem::getStatus, Constants.ITEM_STATUS_PENDING)
        );
        Long pendingFound = foundItemMapper.selectCount(
                new LambdaQueryWrapper<FoundItem>().eq(FoundItem::getStatus, Constants.ITEM_STATUS_PENDING)
        );
        stats.put("pendingMatch", pendingLost + pendingFound);

        // 已认领数
        Long claimedCount = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>().eq(LostItem::getStatus, Constants.ITEM_STATUS_CLAIMED)
        );
        stats.put("claimedCount", claimedCount);

        // 本月新增
        LocalDateTime monthStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        Long monthLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>().ge(LostItem::getCreatedAt, monthStart)
        );
        Long monthFound = foundItemMapper.selectCount(
                new LambdaQueryWrapper<FoundItem>().ge(FoundItem::getCreatedAt, monthStart)
        );
        stats.put("monthLost", monthLost);
        stats.put("monthFound", monthFound);

        return stats;
    }

    @Override
    public Map<String, Object> getCategoryStats() {
        Map<String, Object> result = new HashMap<>();

        // 这里简化处理，实际应该查询数据库统计
        // 可以使用SQL进行分组统计
        result.put("categories", getCategoryCounts());

        return result;
    }

    @Override
    public Map<String, Object> getSuccessRate() {
        Map<String, Object> result = new HashMap<>();

        // 本月认领成功率
        LocalDateTime monthStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();

        Long monthTotalLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>()
                        .ge(LostItem::getCreatedAt, monthStart)
        );

        Long monthClaimedLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>()
                        .ge(LostItem::getCreatedAt, monthStart)
                        .eq(LostItem::getStatus, Constants.ITEM_STATUS_CLAIMED)
        );

        double monthRate = monthTotalLost > 0 ? (double) monthClaimedLost / monthTotalLost * 100 : 0;
        result.put("monthRate", Math.round(monthRate * 100) / 100.0);
        result.put("monthTotal", monthTotalLost);
        result.put("monthClaimed", monthClaimedLost);

        // 本学期认领成功率 (假设学期从2月或9月开始)
        LocalDate now = LocalDate.now();
        LocalDate semesterStart;
        if (now.getMonthValue() >= 2 && now.getMonthValue() <= 8) {
            semesterStart = LocalDate.of(now.getYear(), 2, 1);
        } else {
            semesterStart = LocalDate.of(now.getYear(), 9, 1);
        }

        Long semesterTotalLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>()
                        .ge(LostItem::getCreatedAt, semesterStart.atStartOfDay())
        );

        Long semesterClaimedLost = lostItemMapper.selectCount(
                new LambdaQueryWrapper<LostItem>()
                        .ge(LostItem::getCreatedAt, semesterStart.atStartOfDay())
                        .eq(LostItem::getStatus, Constants.ITEM_STATUS_CLAIMED)
        );

        double semesterRate = semesterTotalLost > 0 ? (double) semesterClaimedLost / semesterTotalLost * 100 : 0;
        result.put("semesterRate", Math.round(semesterRate * 100) / 100.0);
        result.put("semesterTotal", semesterTotalLost);
        result.put("semesterClaimed", semesterClaimedLost);

        return result;
    }

    @Override
    public Map<String, Object> getRecentLostItems() {
        Map<String, Object> result = new HashMap<>();

        List<LostItem> items = lostItemMapper.selectList(
                new LambdaQueryWrapper<LostItem>()
                        .orderByDesc(LostItem::getCreatedAt)
                        .last("LIMIT 10")
        );

        result.put("items", items);
        return result;
    }

    @Override
    public Map<String, Object> getRecentFoundItems() {
        Map<String, Object> result = new HashMap<>();

        List<FoundItem> items = foundItemMapper.selectList(
                new LambdaQueryWrapper<FoundItem>()
                        .orderByDesc(FoundItem::getCreatedAt)
                        .last("LIMIT 10")
        );

        result.put("items", items);
        return result;
    }

    private Map<String, Long> getCategoryCounts() {
        Map<String, Long> counts = new HashMap<>();

        // 简化处理，实际应该JOIN查询
        counts.put("电子设备", 0L);
        counts.put("书本文具", 0L);
        counts.put("证件卡类", 0L);
        counts.put("衣物饰品", 0L);
        counts.put("钥匙雨伞", 0L);
        counts.put("钱包背包", 0L);
        counts.put("体育用品", 0L);
        counts.put("其他物品", 0L);

        return counts;
    }
}
