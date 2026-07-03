package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.MatchRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MatchRecordMapper extends BaseMapper<MatchRecord> {

    @Select("SELECT m.*, " +
            "l.title as lost_title, l.lost_location, l.lost_time, l.user_id as lost_user_id, " +
            "f.title as found_title, f.found_location, f.found_time, f.user_id as found_user_id " +
            "FROM match_record m " +
            "LEFT JOIN lost_item l ON m.lost_item_id = l.id " +
            "LEFT JOIN found_item f ON m.found_item_id = f.id " +
            "WHERE m.lost_item_id = #{lostItemId} " +
            "ORDER BY m.similarity_score DESC")
    List<MatchRecord> selectByLostItemId(@Param("lostItemId") Long lostItemId);

    @Select("SELECT m.*, " +
            "l.title as lost_title, l.lost_location, l.lost_time, l.user_id as lost_user_id, " +
            "f.title as found_title, f.found_location, f.found_time, f.user_id as found_user_id " +
            "FROM match_record m " +
            "LEFT JOIN lost_item l ON m.lost_item_id = l.id " +
            "LEFT JOIN found_item f ON m.found_item_id = f.id " +
            "WHERE m.found_item_id = #{foundItemId} " +
            "ORDER BY m.similarity_score DESC")
    List<MatchRecord> selectByFoundItemId(@Param("foundItemId") Long foundItemId);
}
