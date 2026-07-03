package com.wyr.my_class_project.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 用户相关错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_ALREADY_EXISTS(1003, "用户已存在"),
    USER_DISABLED(1004, "用户已被禁用"),

    // 参数错误
    PARAM_ERROR(2001, "参数错误"),
    PARAM_MISSING(2002, "参数缺失"),

    // 业务错误
    ITEM_NOT_FOUND(3001, "物品不存在"),
    ITEM_STATUS_ERROR(3002, "物品状态异常"),
    CLAIM_ALREADY_EXISTS(3003, "已存在认领申请"),
    CLAIM_NOT_FOUND(3004, "认领申请不存在"),
    MATCH_NOT_FOUND(3005, "匹配记录不存在"),

    // 权限错误
    UNAUTHORIZED(4001, "未登录或登录已过期"),
    FORBIDDEN(4002, "没有权限"),

    // 系统错误
    SYSTEM_ERROR(5001, "系统错误"),
    FILE_UPLOAD_ERROR(5002, "文件上传失败");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
