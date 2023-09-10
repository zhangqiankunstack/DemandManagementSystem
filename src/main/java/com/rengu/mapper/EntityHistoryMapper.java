package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.vo.EntityHistoryRelationship;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttributeEntityVo;
import com.rengu.entity.vo.ValueAttributeHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName EntityHistoryMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Mapper
public interface EntityHistoryMapper extends BaseMapper<EntityHistoryModel> {



//    @Select("SELECT DISTINCT relationship_id, " +
//            "entity_history_id1, entity1.entity_name AS entity_name1," +
//            "entity_history_id2, entity2.entity_name AS entity_name2," +
//            "relationship_type, entity1.entity_type AS entity_type " +
//            "FROM relationship_history INNER JOIN entity_history entity1 ON relationship_history.entity_history_id1 = entity1.entity_historyid INNER JOIN entity_history entity2 ON relationship_history.entity_history_id2 = entity2.entity_historyid WHERE entity1.entity_historyid = #{entity_historyid} AND (#{keyWord} IS NULL OR entity1.entity_type LIKE CONCAT('%', #{keyWord}, '%'))")
    List<EntityHistoryRelationship> getEntityHistoryRelationships(@Param("entity_historyid") String entityId, @Param("keyWord") String entityType);





    @Update("UPDATE entity_history SET isTop = #{isTop} WHERE entity_id = #{entityId} AND entity_historyid != #{excludeId}")
    void updateOtherIsTopToZero(@Param("entityId") String entityId, @Param("isTop") Integer isTop, @Param("excludeId") String excludeId);


    /**
     * 查实体的关联关系
     * @param entityHistoryId
     * @return
     */
    List<EntityHistoryRelationship> getRelationshipEntitiesByEntityHistoryId(String entityHistoryId, String entityType);



    List<ValueAttributeEntityVo> getValueAttribute(String entityHistoryId);






    void updateBatchIsTopById(@Param("entityHistories") List<EntityHistoryModel> entityHistories);


    @Select("SELECT * FROM entity_history WHERE entity_id = #{entityId} AND entity_historyid != #{entityHistoryid}")
    List<EntityHistoryModel> getEntityHistoryByEntityId(@Param("entityId") String entityId, @Param("entityHistoryid") String entityHistoryid);



    @Select(" SELECT * FROM entity_history WHERE entity_historyid = #{entityHistoryid}")
   EntityHistoryModel getEntityHistoryByEntityHistoryId(String  entityHistoryid);




    void updateOtherIsTop(String entityId,String entityHistoryid);



    void updateOtherChanges(String entityId,String entityHistoryid);


}
