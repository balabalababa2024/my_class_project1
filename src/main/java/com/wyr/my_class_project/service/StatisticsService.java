package com.wyr.my_class_project.service;

import java.util.Map;

public interface StatisticsService {

    /**
     * 获取首页统计数据
     */
    Map<String, Object> getDashboardStats();

    /**
     * 获取分类统计
     */
    Map<String, Object> getCategoryStats();

    /**
     * 获取本月/本学期认领成功率
     */
    Map<String, Object> getSuccessRate();

    /**
     * 获取近期失物公告
     */
    Map<String, Object> getRecentLostItems();

    /**
     * 获取近期拾物公告
     */
    Map<String, Object> getRecentFoundItems();
}
