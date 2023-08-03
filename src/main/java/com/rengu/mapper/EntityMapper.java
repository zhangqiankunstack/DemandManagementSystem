package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.rengu.entity.EntityModel;
import com.rengu.entity.vo.EntityRelationship;
import org.apache.ibatis.annotations.Mapper;
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
	@Select("SELECT entity.entity_name, relationship.entity_id2 " +
			"FROM relationship " +
			"INNER JOIN entity ON relationship.entity_id2 = entity.entity_id " +
			"WHERE relationship.entity_id1 = #{entityId1}")
	List<EntityRelationship> getEntityRelationships(String entityId1);


}
