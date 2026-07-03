package com.wyr.my_class_project.service;

import com.wyr.my_class_project.entity.ClaimRequest;

import java.util.List;

public interface ClaimService {

    /**
     * 发起认领申请
     */
    ClaimRequest create(ClaimRequest claimRequest);

    /**
     * 确认认领(拾取者)
     */
    void confirm(Long claimId, Long userId);

    /**
     * 拒绝认领(拾取者)
     */
    void reject(Long claimId, Long userId);

    /**
     * 取消认领(认领者)
     */
    void cancel(Long claimId, Long userId);

    /**
     * 管理员处理争议
     */
    void adminHandle(Long claimId, Integer status, String remark);

    /**
     * 获取用户的认领申请(作为认领者)
     */
    List<ClaimRequest> getByClaimerId(Long userId);

    /**
     * 获取用户的认领申请(作为拾取者)
     */
    List<ClaimRequest> getByOwnerId(Long userId);

    /**
     * 根据ID获取认领详情
     */
    ClaimRequest getById(Long id);

    /**
     * 获取所有认领申请(管理员)
     */
    List<ClaimRequest> getAll();
}
