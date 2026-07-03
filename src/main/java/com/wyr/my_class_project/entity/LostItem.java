package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lost_item")
public class LostItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private Long categoryId;

    private String description;

    private String lostLocation;

    private LocalDateTime lostTime;

    private String contactInfo;

    private String imageUrl;

    /**
     * 状态: 0-待匹配, 1-待认领, 2-已认领, 3-已过期
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 非数据库字段
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private Integer matchCount;
}
