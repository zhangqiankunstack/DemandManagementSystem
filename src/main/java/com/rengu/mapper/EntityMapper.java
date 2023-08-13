package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.rengu.entity.EntityModel;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.entity.vo.EntityRelationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName EntityMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Mapper
public interface EntityMapper extends BaseMapper<EntityModel> {
//        @Select("SELECT DISTINCT entity.entity_name, relationship.entity_id2 " +
//            "FROM relationship " +
//            "INNER JOIN entity ON relationship.entity_id2 = entity.entity_id " +
//            "WHERE relationship.entity_id1 = #{entityId}")
    @Select("SELECT DISTINCT relationship.relationship_id,relationship.entity_id1,entity.entity_name as entity_name1 ,relationship.entity_id2,entity.entity_name as entity_name2 FROM relationship INNER JOIN entity ON relationship.entity_id2 = entity.entity_id OR relationship.entity_id1 = entity.entity_id WHERE relationship.entity_id1 = #{entityId} AND (#{keyWord} IS NULL OR entity.entity_name LIKE CONCAT('%', #{keyWord}, '%'))")
    List<EntityRelationship> getEntityRelationships(@Param("entityId") String entityId,@Param("keyWord")String keyWord);


}
