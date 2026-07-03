package com.wyr.my_class_project.service.impl;

import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.entity.ClaimRequest;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.entity.Notification;
import com.wyr.my_class_project.mapper.ClaimRequestMapper;
import com.wyr.my_class_project.mapper.FoundItemMapper;
import com.wyr.my_class_project.mapper.LostItemMapper;
import com.wyr.my_class_project.service.ClaimService;
import com.wyr.my_class_project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimRequestMapper claimRequestMapper;

    @Autowired
    private LostItemMapper lostItemMapper;

    @Autowired
    private FoundItemMapper foundItemMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public ClaimRequest create(ClaimRequest claimRequest) {
        // 检查是否已存在认领申请
        Long count = claimRequestMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ClaimRequest>()
                        .eq(ClaimRequest::getLostItemId, claimRequest.getLostItemId())
                        .eq(ClaimRequest::getFoundItemId, claimRequest.getFoundItemId())
                        .eq(ClaimRequest::getClaimerId, claimRequest.getClaimerId())
                        .ne(ClaimRequest::getStatus, Constants.CLAIM_STATUS_CANCELLED)
        );

        if (count > 0) {
            throw new BusinessException(ResultCode.CLAIM_ALREADY_EXISTS);
        }

        claimRequest.setStatus(Constants.CLAIM_STATUS_PENDING);
        claimRequestMapper.insert(claimRequest);

        // 发送通知给拾取者
        LostItem lostItem = lostItemMapper.selectById(claimRequest.getLostItemId());
        Notification notification = new Notification();
        notification.setUserId(claimRequest.getOwnerId());
        notification.setTitle("新的认领申请");
        notification.setContent("有人认领了您拾到的 \"" + lostItem.getTitle() + "\"，请及时处理。");
        notification.setType(Constants.NOTIFICATION_TYPE_CLAIM);
        notification.setRelatedId(claimRequest.getId());
        notificationService.send(notification);

        return claimRequest;
    }

    @Override
    @Transactional
    public void confirm(Long claimId, Long userId) {
        ClaimRequest claim = claimRequestMapper.selectById(claimId);
        if (claim == null) {
            throw new BusinessException(ResultCode.CLAIM_NOT_FOUND);
        }

        // 只有拾取者可以确认
        if (!claim.getOwnerId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (claim.getStatus() != Constants.CLAIM_STATUS_PENDING) {
            throw new BusinessException(ResultCode.ITEM_STATUS_ERROR);
        }

        // 更新认领状态
        claim.setStatus(Constants.CLAIM_STATUS_CONFIRMED);
        claimRequestMapper.updateById(claim);

        // 更新物品状态
        LostItem lostItem = lostItemMapper.selectById(claim.getLostItemId());
        lostItem.setStatus(Constants.ITEM_STATUS_CLAIMED);
        lostItemMapper.updateById(lostItem);

        FoundItem foundItem = foundItemMapper.selectById(claim.getFoundItemId());
        foundItem.setStatus(Constants.ITEM_STATUS_CLAIMED);
        foundItemMapper.updateById(foundItem);

        // 通知认领者
        Notification notification = new Notification();
        notification.setUserId(claim.getClaimerId());
        notification.setTitle("认领成功");
        notification.setContent("您申请认领的 \"" + lostItem.getTitle() + "\" 已确认，请及时联系拾取者取回物品。");
        notification.setType(Constants.NOTIFICATION_TYPE_CLAIM);
        notification.setRelatedId(claim.getId());
        notificationService.send(notification);
    }

    @Override
    @Transactional
    public void reject(Long claimId, Long userId) {
        ClaimRequest claim = claimRequestMapper.selectById(claimId);
        if (claim == null) {
            throw new BusinessException(ResultCode.CLAIM_NOT_FOUND);
        }

        // 只有拾取者可以拒绝
        if (!claim.getOwnerId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (claim.getStatus() != Constants.CLAIM_STATUS_PENDING) {
            throw new BusinessException(ResultCode.ITEM_STATUS_ERROR);
        }

        claim.setStatus(Constants.CLAIM_STATUS_REJECTED);
        claimRequestMapper.updateById(claim);

        // 通知认领者
        LostItem lostItem = lostItemMapper.selectById(claim.getLostItemId());
        Notification notification = new Notification();
        notification.setUserId(claim.getClaimerId());
        notification.setTitle("认领被拒绝");
        notification.setContent("您申请认领的 \"" + lostItem.getTitle() + "\" 被拒绝，您可以重新发起认领或联系管理员。");
        notification.setType(Constants.NOTIFICATION_TYPE_CLAIM);
        notification.setRelatedId(claim.getId());
        notificationService.send(notification);
    }

    @Override
    @Transactional
    public void cancel(Long claimId, Long userId) {
        ClaimRequest claim = claimRequestMapper.selectById(claimId);
        if (claim == null) {
            throw new BusinessException(ResultCode.CLAIM_NOT_FOUND);
        }

        // 只有认领者可以取消
        if (!claim.getClaimerId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (claim.getStatus() != Constants.CLAIM_STATUS_PENDING) {
            throw new BusinessException(ResultCode.ITEM_STATUS_ERROR);
        }

        claim.setStatus(Constants.CLAIM_STATUS_CANCELLED);
        claimRequestMapper.updateById(claim);
    }

    @Override
    @Transactional
    public void adminHandle(Long claimId, Integer status, String remark) {
        ClaimRequest claim = claimRequestMapper.selectById(claimId);
        if (claim == null) {
            throw new BusinessException(ResultCode.CLAIM_NOT_FOUND);
        }

        claim.setStatus(status);
        claim.setAdminRemark(remark);
        claimRequestMapper.updateById(claim);

        // 如果确认认领，更新物品状态
        if (status == Constants.CLAIM_STATUS_CONFIRMED) {
            LostItem lostItem = lostItemMapper.selectById(claim.getLostItemId());
            lostItem.setStatus(Constants.ITEM_STATUS_CLAIMED);
            lostItemMapper.updateById(lostItem);

            FoundItem foundItem = foundItemMapper.selectById(claim.getFoundItemId());
            foundItem.setStatus(Constants.ITEM_STATUS_CLAIMED);
            foundItemMapper.updateById(foundItem);
        }

        // 通知双方
        String statusText = status == Constants.CLAIM_STATUS_CONFIRMED ? "已确认" : "已拒绝";
        String content = "您的认领申请已被管理员" + statusText + "。" + (remark != null ? " 备注：" + remark : "");

        Notification claimerNotification = new Notification();
        claimerNotification.setUserId(claim.getClaimerId());
        claimerNotification.setTitle("认领申请处理结果");
        claimerNotification.setContent(content);
        claimerNotification.setType(Constants.NOTIFICATION_TYPE_CLAIM);
        claimerNotification.setRelatedId(claim.getId());
        notificationService.send(claimerNotification);

        Notification ownerNotification = new Notification();
        ownerNotification.setUserId(claim.getOwnerId());
        ownerNotification.setTitle("认领申请处理结果");
        ownerNotification.setContent(content);
        ownerNotification.setType(Constants.NOTIFICATION_TYPE_CLAIM);
        ownerNotification.setRelatedId(claim.getId());
        notificationService.send(ownerNotification);
    }

    @Override
    public List<ClaimRequest> getByClaimerId(Long userId) {
        return claimRequestMapper.selectByClaimerId(userId);
    }

    @Override
    public List<ClaimRequest> getByOwnerId(Long userId) {
        return claimRequestMapper.selectByOwnerId(userId);
    }

    @Override
    public ClaimRequest getById(Long id) {
        return claimRequestMapper.selectById(id);
    }

    @Override
    public List<ClaimRequest> getAll() {
        return claimRequestMapper.selectAll();
    }
}
