package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("item_image")
public class ItemImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long itemId;

    /**
     * 物品类型: 1-失物, 2-拾物
     */
    private Integer itemType;

    private String imageUrl;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
