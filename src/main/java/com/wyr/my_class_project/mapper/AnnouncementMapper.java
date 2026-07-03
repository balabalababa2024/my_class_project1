package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    @Select("SELECT a.*, u.name as publisher_name " +
            "FROM announcement a " +
            "LEFT JOIN users u ON a.publisher_id = u.id " +
            "WHERE a.status = 1 " +
            "ORDER BY a.is_top DESC, a.created_at DESC")
    List<Announcement> selectPublished();

    @Select("SELECT a.*, u.name as publisher_name " +
            "FROM announcement a " +
            "LEFT JOIN users u ON a.publisher_id = u.id " +
            "WHERE a.id = #{id}")
    Announcement selectDetail(@Param("id") Long id);
}
