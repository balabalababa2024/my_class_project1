package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.dto.FoundItemQuery;
import com.wyr.my_class_project.entity.FoundItem;
import com.wyr.my_class_project.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/found")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    /**
     * 发布拾物
     */
    @PostMapping("/create")
    public Result<FoundItem> create(HttpServletRequest request, @RequestBody FoundItem foundItem) {
        Long userId = (Long) request.getAttribute("userId");
        foundItem.setUserId(userId);
        FoundItem created = foundItemService.create(foundItem);
        return Result.success(created);
    }

    /**
     * 更新拾物
     */
    @PutMapping("/update")
    public Result<FoundItem> update(HttpServletRequest request, @RequestBody FoundItem foundItem) {
        Long userId = (Long) request.getAttribute("userId");
        foundItem.setUserId(userId);
        FoundItem updated = foundItemService.update(foundItem);
        return Result.success(updated);
    }

    /**
     * 删除拾物
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        foundItemService.delete(id, userId);
        return Result.success();
    }

    /**
     * 获取拾物详情
     */
    @GetMapping("/detail/{id}")
    public Result<FoundItem> getById(@PathVariable Long id) {
        FoundItem item = foundItemService.getById(id);
        return Result.success(item);
    }

    /**
     * 分页查询拾物
     */
    @GetMapping("/page")
    public Result<PageResult<FoundItem>> page(FoundItemQuery query) {
        PageResult<FoundItem> result = foundItemService.page(query);
        return Result.success(result);
    }

    /**
     * 获取用户的拾物列表
     */
    @GetMapping("/my")
    public Result<List<FoundItem>> getMy(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<FoundItem> items = foundItemService.getByUserId(userId);
        return Result.success(items);
    }

    /**
     * 关键词搜索
     */
    @GetMapping("/search")
    public Result<List<FoundItem>> search(@RequestParam String keyword) {
        List<FoundItem> items = foundItemService.search(keyword);
        return Result.success(items);
    }
}
