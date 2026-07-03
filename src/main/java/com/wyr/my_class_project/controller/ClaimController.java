package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.entity.ClaimRequest;
import com.wyr.my_class_project.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    /**
     * 发起认领申请
     */
    @PostMapping("/create")
    public Result<ClaimRequest> create(HttpServletRequest request, @RequestBody ClaimRequest claimRequest) {
        Long userId = (Long) request.getAttribute("userId");
        claimRequest.setClaimerId(userId);
        ClaimRequest created = claimService.create(claimRequest);
        return Result.success(created);
    }

    /**
     * 确认认领(拾取者)
     */
    @PostMapping("/confirm/{claimId}")
    public Result<Void> confirm(HttpServletRequest request, @PathVariable Long claimId) {
        Long userId = (Long) request.getAttribute("userId");
        claimService.confirm(claimId, userId);
        return Result.success();
    }

    /**
     * 拒绝认领(拾取者)
     */
    @PostMapping("/reject/{claimId}")
    public Result<Void> reject(HttpServletRequest request, @PathVariable Long claimId) {
        Long userId = (Long) request.getAttribute("userId");
        claimService.reject(claimId, userId);
        return Result.success();
    }

    /**
     * 取消认领(认领者)
     */
    @PostMapping("/cancel/{claimId}")
    public Result<Void> cancel(HttpServletRequest request, @PathVariable Long claimId) {
        Long userId = (Long) request.getAttribute("userId");
        claimService.cancel(claimId, userId);
        return Result.success();
    }

    /**
     * 获取我的认领申请(作为认领者)
     */
    @GetMapping("/my/claim")
    public Result<List<ClaimRequest>> getMyClaims(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ClaimRequest> claims = claimService.getByClaimerId(userId);
        return Result.success(claims);
    }

    /**
     * 获取我的认领申请(作为拾取者)
     */
    @GetMapping("/my/owner")
    public Result<List<ClaimRequest>> getMyOwnerClaims(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ClaimRequest> claims = claimService.getByOwnerId(userId);
        return Result.success(claims);
    }

    /**
     * 获取认领详情
     */
    @GetMapping("/detail/{id}")
    public Result<ClaimRequest> getById(@PathVariable Long id) {
        ClaimRequest claim = claimService.getById(id);
        return Result.success(claim);
    }

    /**
     * 管理员处理争议
     */
    @PostMapping("/admin/handle")
    public Result<Void> adminHandle(@RequestBody Map<String, Object> params) {
        Long claimId = Long.valueOf(params.get("claimId").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        String remark = params.get("remark") != null ? params.get("remark").toString() : null;
        claimService.adminHandle(claimId, status, remark);
        return Result.success();
    }

    /**
     * 获取所有认领申请(管理员)
     */
    @GetMapping("/admin/all")
    public Result<List<ClaimRequest>> getAll() {
        List<ClaimRequest> claims = claimService.getAll();
        return Result.success(claims);
    }
}
