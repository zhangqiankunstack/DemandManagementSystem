package com.rengu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.RelationshipModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName RelationshipMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Mapper
public interface RelationshipMapper extends BaseMapper<RelationshipModel> {



    List<RelationshipModel> getRelationshipsByEntityIds(List<String> entityIds);

}
