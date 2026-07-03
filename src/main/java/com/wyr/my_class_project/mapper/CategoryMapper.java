package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
