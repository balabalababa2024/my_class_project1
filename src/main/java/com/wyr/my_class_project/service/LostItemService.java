package com.wyr.my_class_project.service;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.dto.LostItemQuery;

import java.util.List;

public interface LostItemService {

    /**
     * 发布失物
     */
    LostItem create(LostItem lostItem);

    /**
     * 更新失物
     */
    LostItem update(LostItem lostItem);

    /**
     * 删除失物
     */
    void delete(Long id, Long userId);

    /**
     * 根据ID获取失物详情
     */
    LostItem getById(Long id);

    /**
     * 分页查询失物
     */
    PageResult<LostItem> page(LostItemQuery query);

    /**
     * 获取用户的失物列表
     */
    List<LostItem> getByUserId(Long userId);

    /**
     * 根据状态获取失物列表
     */
    List<LostItem> getByStatus(Integer status);

    /**
     * 关键词搜索失物
     */
    List<LostItem> search(String keyword);

    /**
     * 更新失物状态
     */
    void updateStatus(Long id, Integer status);
}
