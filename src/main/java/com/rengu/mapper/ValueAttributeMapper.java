package com.rengu.mapper;


import com.rengu.entity.vo.ValueAttribute;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ValueAttributeMapper {
    @Select("SELECT v.value, v.attribute_id,a.attribute_name, v.value_id as valueId " +
            "FROM value v " +
            "JOIN entity e ON v.entity_id = e.entity_id " +
            "JOIN attribute a ON v.attribute_id = a.attribute_id " +
            "WHERE e.entity_id = #{entityId} and (#{keyWord}::text IS NULL OR a.attribute_name LIKE CONCAT('%', #{keyWord}::text, '%') OR v.value Like CONCAT('%', #{keyWord}::text, '%'))")
//    @Results({
//            @Result(property = "value", column = "value"),
//            @Result(property = "attribute.attributeId", column = "attribute_id")
//    })
    List<ValueAttribute> findValueAttributesByEntityId(@Param("entityId") String entityId, @Param("keyWord") String keyWord);
}
