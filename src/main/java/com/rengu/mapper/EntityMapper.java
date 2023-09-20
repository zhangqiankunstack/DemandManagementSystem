package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.rengu.entity.EntityModel;
import com.rengu.entity.vo.EntityExportVo;
import com.rengu.entity.vo.EntityRelationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName EntityMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Mapper
public interface EntityMapper extends BaseMapper<EntityModel> {
//    @Select("SELECT DISTINCT relationship.relationship_id,relationship.entity_id1,entity.entity_name as entity_name1 ,relationship.entity_id2,entity.entity_name as entity_name2,relationship.relationship_type as relationship_type FROM relationship INNER JOIN entity ON relationship.entity_id2 = entity.entity_id OR relationship.entity_id1 = entity.entity_id WHERE relationship.entity_id1 = #{entityId} AND (#{keyWord} IS NULL OR entity.entity_name LIKE CONCAT('%', #{keyWord}, '%'))")
    @Select("SELECT DISTINCT relationship.relationship_id,relationship.entity_id1,entity.entity_name as entity_name1 ,relationship.entity_id2,entity.entity_name as entity_name2,relationship.relationship_type as relationship_type,entity.entity_type AS entity_type FROM relationship INNER JOIN entity ON relationship.entity_id2 = entity.entity_id OR relationship.entity_id1 = entity.entity_id WHERE entity.entity_id = #{entityId} AND (#{keyWord}::text IS NULL OR entity.entity_type LIKE CONCAT('%', #{keyWord}::text, '%'))")
    List<EntityRelationship> getEntityRelationships(@Param("entityId") String entityId, @Param("keyWord") String keyWord);



    @Select("SELECT e.entity_id, e.entity_name, r.file_content as description FROM entity e LEFT JOIN requirement_model r ON e.entity_id = r.entity_id WHERE e.entity_type = #{entityType}")
    List<EntityExportVo> getEntityWithDescriptionByType(@Param("entityType") String entityType);


}
