package com.wyr.my_class_project.service;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.entity.Notification;

import java.util.List;

public interface NotificationService {

    /**
     * 发送通知
     */
    void send(Notification notification);

    /**
     * 批量发送通知
     */
    void sendBatch(List<Notification> notifications);

    /**
     * 获取用户通知列表
     */
    PageResult<Notification> getByUserId(Long userId, Integer page, Integer size);

    /**
     * 获取未读通知数量
     */
    Integer getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     */
    void markAsRead(Long id, Long userId);

    /**
     * 标记所有通知为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    void delete(Long id, Long userId);
}
