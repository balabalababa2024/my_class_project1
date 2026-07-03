package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.entity.Notification;
import com.wyr.my_class_project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取通知列表
     */
    @GetMapping("/list")
    public Result<PageResult<Notification>> list(HttpServletRequest request,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<Notification> result = notificationService.getByUserId(userId, page, size);
        return Result.success(result);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Integer count = notificationService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/read/{id}")
    public Result<Void> markAsRead(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        notificationService.markAsRead(id, userId);
        return Result.success();
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/read/all")
    public Result<Void> markAllAsRead(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        notificationService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        notificationService.delete(id, userId);
        return Result.success();
    }
}
