package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String studentId;

    private String phone;

    private String password;

    private String name;

    private String avatar;

    private String college;

    private String email;

    /**
     * 角色: 0-普通用户, 1-管理员
     */
    private Integer role;

    /**
     * 状态: 0-禁用, 1-正常
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
