package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("claim_request")
public class ClaimRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long lostItemId;

    private Long foundItemId;

    /**
     * 认领者ID(失主)
     */
    private Long claimerId;

    /**
     * 拾取者ID
     */
    private Long ownerId;

    /**
     * 认领说明
     */
    private String description;

    /**
     * 证明图片URL
     */
    private String proofImage;

    /**
     * 状态: 0-待确认, 1-已确认, 2-已拒绝, 3-已取消
     */
    private Integer status;

    /**
     * 管理员备注
     */
    private String adminRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 非数据库字段
    @TableField(exist = false)
    private String claimerName;

    @TableField(exist = false)
    private String ownerName;

    @TableField(exist = false)
    private String ownerPhone;

    @TableField(exist = false)
    private String lostTitle;

    @TableField(exist = false)
    private String foundTitle;

    @TableField(exist = false)
    private String foundStorageLocation;

    @TableField(exist = false)
    private LostItem lostItem;

    @TableField(exist = false)
    private FoundItem foundItem;
}
