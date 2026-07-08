package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.annotation.RequireAdmin;
import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.entity.Announcement;
import com.wyr.my_class_project.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 获取已发布的公告列表
     */
    @GetMapping("/list")
    public Result<List<Announcement>> list() {
        List<Announcement> announcements = announcementService.getPublished();
        return Result.success(announcements);
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/detail/{id}")
    public Result<Announcement> detail(@PathVariable Long id) {
        Announcement announcement = announcementService.getById(id);
        return Result.success(announcement);
    }

    /**
     * 创建公告(管理员)
     */
    @RequireAdmin
    @PostMapping("/create")
    public Result<Announcement> create(HttpServletRequest request, @RequestBody Announcement announcement) {
        Long userId = (Long) request.getAttribute("userId");
        announcement.setPublisherId(userId);
        Announcement created = announcementService.create(announcement);
        return Result.success(created);
    }

    /**
     * 更新公告(管理员)
     */
    @RequireAdmin
    @PutMapping("/update")
    public Result<Announcement> update(@RequestBody Announcement announcement) {
        Announcement updated = announcementService.update(announcement);
        return Result.success(updated);
    }

    /**
     * 删除公告(管理员)
     */
    @RequireAdmin
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.success();
    }

    /**
     * 获取所有公告(管理员)
     */
    @RequireAdmin
    @GetMapping("/admin/all")
    public Result<List<Announcement>> getAll() {
        List<Announcement> announcements = announcementService.getAll();
        return Result.success(announcements);
    }

    /**
     * 更新公告状态(管理员)
     */
    @RequireAdmin
    @PostMapping("/admin/status")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        announcementService.updateStatus(id, status);
        return Result.success();
    }
}
