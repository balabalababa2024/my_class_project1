package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.FoundItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FoundItemMapper extends BaseMapper<FoundItem> {

    @Select("SELECT f.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.found_item_id = f.id) as match_count " +
            "FROM found_item f " +
            "LEFT JOIN users u ON f.user_id = u.id " +
            "LEFT JOIN category c ON f.category_id = c.id " +
            "WHERE f.status = #{status} " +
            "ORDER BY f.created_at DESC")
    List<FoundItem> selectByStatus(@Param("status") Integer status);

    @Select("SELECT f.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.found_item_id = f.id) as match_count " +
            "FROM found_item f " +
            "LEFT JOIN users u ON f.user_id = u.id " +
            "LEFT JOIN category c ON f.category_id = c.id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.created_at DESC")
    List<FoundItem> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT f.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.found_item_id = f.id) as match_count " +
            "FROM found_item f " +
            "LEFT JOIN users u ON f.user_id = u.id " +
            "LEFT JOIN category c ON f.category_id = c.id " +
            "WHERE f.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR f.description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR f.found_location LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY f.created_at DESC")
    List<FoundItem> selectByKeyword(@Param("keyword") String keyword);
}
