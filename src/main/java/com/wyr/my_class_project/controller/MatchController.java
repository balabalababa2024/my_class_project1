package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.entity.MatchRecord;
import com.wyr.my_class_project.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * 根据失物ID获取匹配记录
     */
    @GetMapping("/lost/{lostItemId}")
    public Result<List<MatchRecord>> getByLostItemId(@PathVariable Long lostItemId) {
        List<MatchRecord> records = matchService.getByLostItemId(lostItemId);
        return Result.success(records);
    }

    /**
     * 根据拾物ID获取匹配记录
     */
    @GetMapping("/found/{foundItemId}")
    public Result<List<MatchRecord>> getByFoundItemId(@PathVariable Long foundItemId) {
        List<MatchRecord> records = matchService.getByFoundItemId(foundItemId);
        return Result.success(records);
    }

    /**
     * 确认匹配
     */
    @PostMapping("/confirm/{matchId}")
    public Result<Void> confirmMatch(HttpServletRequest request, @PathVariable Long matchId) {
        Long userId = (Long) request.getAttribute("userId");
        matchService.confirmMatch(matchId, userId);
        return Result.success();
    }

    /**
     * 拒绝匹配
     */
    @PostMapping("/reject/{matchId}")
    public Result<Void> rejectMatch(HttpServletRequest request, @PathVariable Long matchId) {
        Long userId = (Long) request.getAttribute("userId");
        matchService.rejectMatch(matchId, userId);
        return Result.success();
    }

    /**
     * 手动触发匹配(管理员)
     */
    @PostMapping("/manual")
    public Result<Void> manualMatch(@RequestBody Map<String, Long> params) {
        Long lostItemId = params.get("lostItemId");
        Long foundItemId = params.get("foundItemId");
        matchService.manualMatch(lostItemId, foundItemId);
        return Result.success();
    }

    /**
     * 执行智能匹配(管理员)
     */
    @PostMapping("/execute")
    public Result<Void> executeMatch() {
        matchService.executeMatch();
        return Result.success();
    }
}
