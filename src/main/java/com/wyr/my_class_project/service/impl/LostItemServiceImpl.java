package com.wyr.my_class_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.dto.LostItemQuery;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.mapper.LostItemMapper;
import com.wyr.my_class_project.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LostItemServiceImpl implements LostItemService {

    @Autowired
    private LostItemMapper lostItemMapper;

    @Override
    @Transactional
    public LostItem create(LostItem lostItem) {
        lostItem.setStatus(Constants.ITEM_STATUS_PENDING);
        lostItemMapper.insert(lostItem);
        return lostItem;
    }

    @Override
    @Transactional
    public LostItem update(LostItem lostItem) {
        LostItem existItem = lostItemMapper.selectById(lostItem.getId());
        if (existItem == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }

        // 只能更新自己的物品
        if (!existItem.getUserId().equals(lostItem.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        lostItemMapper.updateById(lostItem);
        return lostItemMapper.selectById(lostItem.getId());
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        LostItem item = lostItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }

        // 只能删除自己的物品
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        lostItemMapper.deleteById(id);
    }

    @Override
    public LostItem getById(Long id) {
        LostItem item = lostItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        return item;
    }

    @Override
    public PageResult<LostItem> page(LostItemQuery query) {
        Page<LostItem> page = new Page<>(query.getPage(), query.getSize());

        LambdaQueryWrapper<LostItem> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(LostItem::getTitle, query.getKeyword())
                    .or()
                    .like(LostItem::getDescription, query.getKeyword())
                    .or()
                    .like(LostItem::getLostLocation, query.getKeyword())
            );
        }

        if (query.getCategoryId() != null) {
            wrapper.eq(LostItem::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null) {
            wrapper.eq(LostItem::getStatus, query.getStatus());
        }

        if (query.getUserId() != null) {
            wrapper.eq(LostItem::getUserId, query.getUserId());
        }

        wrapper.orderByDesc(LostItem::getCreatedAt);

        Page<LostItem> result = lostItemMapper.selectPage(page, wrapper);

        return new PageResult<>(result.getTotal(), result.getRecords(), query.getPage(), query.getSize());
    }

    @Override
    public List<LostItem> getByUserId(Long userId) {
        return lostItemMapper.selectByUserId(userId);
    }

    @Override
    public List<LostItem> getByStatus(Integer status) {
        return lostItemMapper.selectByStatus(status);
    }

    @Override
    public List<LostItem> search(String keyword) {
        return lostItemMapper.selectByKeyword(keyword);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        LostItem item = lostItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        item.setStatus(status);
        lostItemMapper.updateById(item);
    }
}
