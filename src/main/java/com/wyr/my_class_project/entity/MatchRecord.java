package com.wyr.my_class_project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("match_record")
public class MatchRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long lostItemId;

    private Long foundItemId;

    /**
     * 相似度分数(0-100)
     */
    private Double similarityScore;

    /**
     * 匹配类型: name-名称, category-类别, location-地点, time-时间
     */
    private String matchType;

    /**
     * 状态: 0-待确认, 1-已确认, 2-已拒绝
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 非数据库字段
    @TableField(exist = false)
    private LostItem lostItem;

    @TableField(exist = false)
    private FoundItem foundItem;
}
