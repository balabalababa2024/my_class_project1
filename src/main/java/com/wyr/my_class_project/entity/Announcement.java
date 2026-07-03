package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class Announcement {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Long publisherId;

    /**
     * 是否置顶: 0-否, 1-是
     */
    private Integer isTop;

    /**
     * 状态: 0-草稿, 1-已发布, 2-已下架
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 非数据库字段
    @TableField(exist = false)
    private String publisherName;
}
