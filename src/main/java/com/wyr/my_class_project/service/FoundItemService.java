package com.wyr.my_class_project.service;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.dto.FoundItemQuery;

import java.util.List;

public interface FoundItemService {

    /**
     * 发布拾物
     */
    FoundItem create(FoundItem foundItem);

    /**
     * 更新拾物
     */
    FoundItem update(FoundItem foundItem);

    /**
     * 删除拾物
     */
    void delete(Long id, Long userId);

    /**
     * 根据ID获取拾物详情
     */
    FoundItem getById(Long id);

    /**
     * 分页查询拾物
     */
    PageResult<FoundItem> page(FoundItemQuery query);

    /**
     * 获取用户的拾物列表
     */
    List<FoundItem> getByUserId(Long userId);

    /**
     * 根据状态获取拾物列表
     */
    List<FoundItem> getByStatus(Integer status);

    /**
     * 关键词搜索拾物
     */
    List<FoundItem> search(String keyword);

    /**
     * 更新拾物状态
     */
    void updateStatus(Long id, Integer status);
}
