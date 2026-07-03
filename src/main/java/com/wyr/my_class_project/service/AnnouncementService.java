package com.wyr.my_class_project.service;

import com.wyr.my_class_project.entity.Announcement;

import java.util.List;

public interface AnnouncementService {

    /**
     * 创建公告
     */
    Announcement create(Announcement announcement);

    /**
     * 更新公告
     */
    Announcement update(Announcement announcement);

    /**
     * 删除公告
     */
    void delete(Long id);

    /**
     * 根据ID获取公告详情
     */
    Announcement getById(Long id);

    /**
     * 获取已发布的公告列表
     */
    List<Announcement> getPublished();

    /**
     * 获取所有公告(管理员)
     */
    List<Announcement> getAll();

    /**
     * 更新公告状态
     */
    void updateStatus(Long id, Integer status);
}
