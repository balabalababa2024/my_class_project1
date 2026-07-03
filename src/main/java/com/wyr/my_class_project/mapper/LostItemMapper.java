package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.LostItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LostItemMapper extends BaseMapper<LostItem> {

    @Select("SELECT l.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.lost_item_id = l.id) as match_count " +
            "FROM lost_item l " +
            "LEFT JOIN users u ON l.user_id = u.id " +
            "LEFT JOIN category c ON l.category_id = c.id " +
            "WHERE l.status = #{status} " +
            "ORDER BY l.created_at DESC")
    List<LostItem> selectByStatus(@Param("status") Integer status);

    @Select("SELECT l.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.lost_item_id = l.id) as match_count " +
            "FROM lost_item l " +
            "LEFT JOIN users u ON l.user_id = u.id " +
            "LEFT JOIN category c ON l.category_id = c.id " +
            "WHERE l.user_id = #{userId} " +
            "ORDER BY l.created_at DESC")
    List<LostItem> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT l.*, u.name as user_name, c.name as category_name, " +
            "(SELECT COUNT(*) FROM match_record m WHERE m.lost_item_id = l.id) as match_count " +
            "FROM lost_item l " +
            "LEFT JOIN users u ON l.user_id = u.id " +
            "LEFT JOIN category c ON l.category_id = c.id " +
            "WHERE l.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR l.description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR l.lost_location LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY l.created_at DESC")
    List<LostItem> selectByKeyword(@Param("keyword") String keyword);
}
