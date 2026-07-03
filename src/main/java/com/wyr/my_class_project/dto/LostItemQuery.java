package com.wyr.my_class_project.dto;

import lombok.Data;

@Data
public class LostItemQuery {

    private Integer page = 1;

    private Integer size = 10;

    private String keyword;

    private Long categoryId;

    private Integer status;

    private Long userId;
}
