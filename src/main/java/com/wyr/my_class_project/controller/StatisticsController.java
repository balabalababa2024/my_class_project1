package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = statisticsService.getDashboardStats();
        return Result.success(stats);
    }

    /**
     * 获取分类统计
     */
    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryStats() {
        Map<String, Object> stats = statisticsService.getCategoryStats();
        return Result.success(stats);
    }

    /**
     * 获取认领成功率
     */
    @GetMapping("/success-rate")
    public Result<Map<String, Object>> getSuccessRate() {
        Map<String, Object> stats = statisticsService.getSuccessRate();
        return Result.success(stats);
    }

    /**
     * 获取近期失物公告
     */
    @GetMapping("/recent/lost")
    public Result<Map<String, Object>> getRecentLostItems() {
        Map<String, Object> result = statisticsService.getRecentLostItems();
        return Result.success(result);
    }

    /**
     * 获取近期拾物公告
     */
    @GetMapping("/recent/found")
    public Result<Map<String, Object>> getRecentFoundItems() {
        Map<String, Object> result = statisticsService.getRecentFoundItems();
        return Result.success(result);
    }
}
