package com.wyr.my_class_project.common;

public class Constants {

    /**
     * 物品状态
     */
    public static final int ITEM_STATUS_PENDING = 0;  // 待匹配
    public static final int ITEM_STATUS_MATCHED = 1;  // 待认领
    public static final int ITEM_STATUS_CLAIMED = 2;  // 已认领
    public static final int ITEM_STATUS_EXPIRED = 3;  // 已过期

    /**
     * 认领申请状态
     */
    public static final int CLAIM_STATUS_PENDING = 0;   // 待确认
    public static final int CLAIM_STATUS_CONFIRMED = 1;  // 已确认
    public static final int CLAIM_STATUS_REJECTED = 2;   // 已拒绝
    public static final int CLAIM_STATUS_CANCELLED = 3;  // 已取消

    /**
     * 匹配记录状态
     */
    public static final int MATCH_STATUS_PENDING = 0;   // 待确认
    public static final int MATCH_STATUS_CONFIRMED = 1;  // 已确认
    public static final int MATCH_STATUS_REJECTED = 2;   // 已拒绝

    /**
     * 通知类型
     */
    public static final int NOTIFICATION_TYPE_MATCH = 1;    // 匹配通知
    public static final int NOTIFICATION_TYPE_CLAIM = 2;    // 认领通知
    public static final int NOTIFICATION_TYPE_SYSTEM = 3;   // 系统通知
    public static final int NOTIFICATION_TYPE_ANNOUNCEMENT = 4;  // 公告

    /**
     * 用户角色
     */
    public static final int ROLE_USER = 0;       // 普通用户
    public static final int ROLE_ADMIN = 1;      // 管理员

    /**
     * 匹配相似度阈值
     */
    public static final double MATCH_THRESHOLD = 60.0;

    /**
     * 物品过期天数
     */
    public static final int EXPIRE_DAYS = 30;

    /**
     * 图片类型
     */
    public static final int IMAGE_TYPE_LOST = 1;   // 失物图片
    public static final int IMAGE_TYPE_FOUND = 2;  // 拾物图片
}
