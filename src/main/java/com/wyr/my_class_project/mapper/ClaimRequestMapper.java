package com.wyr.my_class_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyr.my_class_project.entity.ClaimRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClaimRequestMapper extends BaseMapper<ClaimRequest> {

    @Select("SELECT cr.*, " +
            "cu.name as claimer_name, ou.name as owner_name, ou.phone as owner_phone, " +
            "l.title as lost_title, f.title as found_title, f.storage_location as found_storage_location " +
            "FROM claim_request cr " +
            "LEFT JOIN users cu ON cr.claimer_id = cu.id " +
            "LEFT JOIN users ou ON cr.owner_id = ou.id " +
            "LEFT JOIN lost_item l ON cr.lost_item_id = l.id " +
            "LEFT JOIN found_item f ON cr.found_item_id = f.id " +
            "WHERE cr.claimer_id = #{userId} " +
            "ORDER BY cr.created_at DESC")
    List<ClaimRequest> selectByClaimerId(@Param("userId") Long userId);

    @Select("SELECT cr.*, " +
            "cu.name as claimer_name, ou.name as owner_name, ou.phone as owner_phone, " +
            "l.title as lost_title, f.title as found_title, f.storage_location as found_storage_location " +
            "FROM claim_request cr " +
            "LEFT JOIN users cu ON cr.claimer_id = cu.id " +
            "LEFT JOIN users ou ON cr.owner_id = ou.id " +
            "LEFT JOIN lost_item l ON cr.lost_item_id = l.id " +
            "LEFT JOIN found_item f ON cr.found_item_id = f.id " +
            "WHERE cr.owner_id = #{userId} " +
            "ORDER BY cr.created_at DESC")
    List<ClaimRequest> selectByOwnerId(@Param("userId") Long userId);

    @Select("SELECT cr.*, " +
            "cu.name as claimer_name, ou.name as owner_name, " +
            "l.title as lost_title, f.title as found_title " +
            "FROM claim_request cr " +
            "LEFT JOIN users cu ON cr.claimer_id = cu.id " +
            "LEFT JOIN users ou ON cr.owner_id = ou.id " +
            "LEFT JOIN lost_item l ON cr.lost_item_id = l.id " +
            "LEFT JOIN found_item f ON cr.found_item_id = f.id " +
            "ORDER BY cr.created_at DESC")
    List<ClaimRequest> selectAll();
}
