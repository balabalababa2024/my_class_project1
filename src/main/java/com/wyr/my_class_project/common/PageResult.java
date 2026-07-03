package com.wyr.my_class_project.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页数据
     */
    private List<T> records;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer pages;

    public PageResult() {}

    public PageResult(Long total, List<T> records, Integer page, Integer size) {
        this.total = total;
        this.records = records;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }
}
