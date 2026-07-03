package com.wyr.my_class_project.service;

import com.wyr.my_class_project.entity.MatchRecord;

import java.util.List;

public interface MatchService {

    /**
     * 执行智能匹配
     */
    void executeMatch();

    /**
     * 根据失物ID获取匹配记录
     */
    List<MatchRecord> getByLostItemId(Long lostItemId);

    /**
     * 根据拾物ID获取匹配记录
     */
    List<MatchRecord> getByFoundItemId(Long foundItemId);

    /**
     * 确认匹配
     */
    void confirmMatch(Long matchId, Long userId);

    /**
     * 拒绝匹配
     */
    void rejectMatch(Long matchId, Long userId);

    /**
     * 手动触发匹配(管理员)
     */
    void manualMatch(Long lostItemId, Long foundItemId);
}
