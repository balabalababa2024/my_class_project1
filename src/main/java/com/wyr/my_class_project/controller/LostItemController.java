package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.PageResult;
import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.dto.LostItemQuery;
import com.wyr.my_class_project.entity.LostItem;
import com.wyr.my_class_project.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/lost")
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    /**
     * 发布失物
     */
    @PostMapping("/create")
    public Result<LostItem> create(HttpServletRequest request, @RequestBody LostItem lostItem) {
        Long userId = (Long) request.getAttribute("userId");
        lostItem.setUserId(userId);
        LostItem created = lostItemService.create(lostItem);
        return Result.success(created);
    }

    /**
     * 更新失物
     */
    @PutMapping("/update")
    public Result<LostItem> update(HttpServletRequest request, @RequestBody LostItem lostItem) {
        Long userId = (Long) request.getAttribute("userId");
        lostItem.setUserId(userId);
        LostItem updated = lostItemService.update(lostItem);
        return Result.success(updated);
    }

    /**
     * 删除失物
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        lostItemService.delete(id, userId);
        return Result.success();
    }

    /**
     * 获取失物详情
     */
    @GetMapping("/detail/{id}")
    public Result<LostItem> getById(@PathVariable Long id) {
        LostItem item = lostItemService.getById(id);
        return Result.success(item);
    }

    /**
     * 分页查询失物
     */
    @GetMapping("/page")
    public Result<PageResult<LostItem>> page(LostItemQuery query) {
        PageResult<LostItem> result = lostItemService.page(query);
        return Result.success(result);
    }

    /**
     * 获取用户的失物列表
     */
    @GetMapping("/my")
    public Result<List<LostItem>> getMy(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<LostItem> items = lostItemService.getByUserId(userId);
        return Result.success(items);
    }

    /**
     * 关键词搜索
     */
    @GetMapping("/search")
    public Result<List<LostItem>> search(@RequestParam String keyword) {
        List<LostItem> items = lostItemService.search(keyword);
        return Result.success(items);
    }
}
