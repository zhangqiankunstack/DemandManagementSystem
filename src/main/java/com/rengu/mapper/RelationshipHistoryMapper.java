package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.RelationshipHistoryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName RelationshipHistoryMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@Mapper
public interface RelationshipHistoryMapper extends BaseMapper<RelationshipHistoryModel> {


    @Select("SELECT * FROM relationship_history WHERE entity_history_id1 = #{entityHistoryid1}")
    List<RelationshipHistoryModel> findByEntityHistoryId(String entityHistoryid1);
}
