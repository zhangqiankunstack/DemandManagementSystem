package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.vo.EntityHistoryRelationship;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttributeEntityVo;
import com.rengu.entity.vo.ValueAttributeHistory;

import java.util.List;
import java.util.Map;


/**
 * @ClassName EntityHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
public interface EntityHistoryService extends IService<EntityHistoryModel> {

    public void copyDataToEntityHistory(List<String> ids);

    Map<String, Object> getAllEntity(String keyWord, Integer pageNumber, Integer pageSize);

    List<EntityHistoryRelationship> getEntityHistoryRelationships(String entityId, String keyWord);


    Map<String, Object> getAllNowEntityHistory(String entityId,String keyWord, Integer pageNumber, Integer pageSize);


    List<ValueAttributeEntityVo> findValueByEntityHistoryId (String entityHistoryId);


    void  recover (String id);

    /**
     * 模糊查询关联关系
     * @param entityHistoryId
     * @param entityType
     * @return
     */
     List<EntityHistoryRelationship> getRelatedEntities(String entityHistoryId, String entityType);


    }
