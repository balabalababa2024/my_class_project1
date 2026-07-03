package com.wyr.my_class_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.dto.FoundItemQuery;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.mapper.FoundItemMapper;
import com.wyr.my_class_project.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FoundItemServiceImpl implements FoundItemService {

    @Autowired
    private FoundItemMapper foundItemMapper;

    @Override
    @Transactional
    public FoundItem create(FoundItem foundItem) {
        foundItem.setStatus(Constants.ITEM_STATUS_PENDING);
        foundItemMapper.insert(foundItem);
        return foundItem;
    }

    @Override
    @Transactional
    public FoundItem update(FoundItem foundItem) {
        FoundItem existItem = foundItemMapper.selectById(foundItem.getId());
        if (existItem == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }

        // 只能更新自己的物品
        if (!existItem.getUserId().equals(foundItem.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        foundItemMapper.updateById(foundItem);
        return foundItemMapper.selectById(foundItem.getId());
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        FoundItem item = foundItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }

        // 只能删除自己的物品
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        foundItemMapper.deleteById(id);
    }

    @Override
    public FoundItem getById(Long id) {
        FoundItem item = foundItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        return item;
    }

    @Override
    public PageResult<FoundItem> page(FoundItemQuery query) {
        Page<FoundItem> page = new Page<>(query.getPage(), query.getSize());

        LambdaQueryWrapper<FoundItem> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(FoundItem::getTitle, query.getKeyword())
                    .or()
                    .like(FoundItem::getDescription, query.getKeyword())
                    .or()
                    .like(FoundItem::getFoundLocation, query.getKeyword())
            );
        }

        if (query.getCategoryId() != null) {
            wrapper.eq(FoundItem::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null) {
            wrapper.eq(FoundItem::getStatus, query.getStatus());
        }

        if (query.getUserId() != null) {
            wrapper.eq(FoundItem::getUserId, query.getUserId());
        }

        wrapper.orderByDesc(FoundItem::getCreatedAt);

        Page<FoundItem> result = foundItemMapper.selectPage(page, wrapper);

        return new PageResult<>(result.getTotal(), result.getRecords(), query.getPage(), query.getSize());
    }

    @Override
    public List<FoundItem> getByUserId(Long userId) {
        return foundItemMapper.selectByUserId(userId);
    }

    @Override
    public List<FoundItem> getByStatus(Integer status) {
        return foundItemMapper.selectByStatus(status);
    }

    @Override
    public List<FoundItem> search(String keyword) {
        return foundItemMapper.selectByKeyword(keyword);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        FoundItem item = foundItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        item.setStatus(status);
        foundItemMapper.updateById(item);
    }
}
