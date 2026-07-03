package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String content;

    /**
     * 类型: 1-匹配通知, 2-认领通知, 3-系统通知, 4-公告
     */
    private Integer type;

    /**
     * 关联ID(匹配ID/认领ID等)
     */
    private Long relatedId;

    /**
     * 是否已读: 0-未读, 1-已读
     */
    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
