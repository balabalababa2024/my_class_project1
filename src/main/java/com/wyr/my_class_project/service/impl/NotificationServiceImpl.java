package com.wyr.my_class_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.entity.Notification;
import com.wyr.my_class_project.mapper.NotificationMapper;
import com.wyr.my_class_project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void send(Notification notification) {
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }

    @Override
    @Transactional
    public void sendBatch(List<Notification> notifications) {
        for (Notification notification : notifications) {
            notification.setIsRead(0);
            notificationMapper.insert(notification);
        }
    }

    @Override
    public PageResult<Notification> getByUserId(Long userId, Integer page, Integer size) {
        Page<Notification> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreatedAt);

        Page<Notification> result = notificationMapper.selectPage(pageParam, wrapper);

        return new PageResult<>(result.getTotal(), result.getRecords(), page, size);
    }

    @Override
    public Integer getUnreadCount(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long id, Long userId) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            notificationMapper.updateById(notification);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null && notification.getUserId().equals(userId)) {
            notificationMapper.deleteById(id);
        }
    }
}
