package com.rengu.mapper;

import com.rengu.entity.vo.ValueAttribute;
import com.rengu.entity.vo.ValueAttributeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ValueAttributeHistoryMapper {

//    @Select("SELECT DISTINCT  v.value, v.attribute_id, a.attribute_name " +
//            "FROM value_history v " +
//            "JOIN entity_history e ON v.entity_historyid = e.entity_id " +
//            "JOIN attribute_history a ON v.attribute_id = a.attribute_id " +
//            "WHERE e.entity_historyid = #{entityId} " +
//            "AND (#{keyWord}::text IS NULL OR a.attribute_name LIKE CONCAT('%', #{keyWord}::text, '%') OR v.value LIKE CONCAT('%', #{keyWord}::text, '%'))")


    @Select("SELECT DISTINCT  v.value, v.attribute_id, a.attribute_name " +
            "FROM value_history v " +
            "JOIN entity_history e ON v.entity_historyid = e.entity_historyid " +
            "JOIN attribute_history a ON v.attribute_id = a.attribute_id " +
            "WHERE v.entity_historyid = #{entityId} " +
            "AND (#{keyWord}::text IS NULL OR a.attribute_name LIKE CONCAT('%', #{keyWord}::text, '%') OR v.value LIKE CONCAT('%', #{keyWord}::text, '%'))")
    List<ValueAttributeHistory> findValueAttributesByEntityId(@Param("entityId") String entityId, @Param("keyWord") String keyWord);

}
