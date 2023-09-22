package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.RelationshipHistoryModel;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * @ClassName RelationshipHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
public interface RelationshipHistoryService extends IService<RelationshipHistoryModel> {
    public void copyDataToRelationshipHistory(List<String> ids, Map<String, EntityHistoryModel> entityHistoryModelMap);

    void deleteByEntityHisId(String entityHisId);
    Map<String, Object> getAllRelationship(String entityId, String keyWord, Integer pageNumber, Integer pageSize);



}
