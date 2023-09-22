package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.entity.vo.EntityHistoryRelationship;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.mapper.RelationshipHistoryMapper;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.service.EntityHistoryService;
import com.rengu.service.RelationshipHistoryService;
import com.rengu.util.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RelationshipHistoryServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@Service
public class RelationshipHistoryServiceImpl extends ServiceImpl<RelationshipHistoryMapper, RelationshipHistoryModel> implements RelationshipHistoryService {

    @Autowired
    private RelationshipMapper relationshipMapper;
    @Autowired
    private EntityHistoryService entityHistoryService;

    @Override
    public void copyDataToRelationshipHistory(List<String> ids, Map<String, EntityHistoryModel> entityHistoryModelMap) {

        // 查询第一个表的数据
        List<RelationshipModel> relationships = relationshipMapper.selectBatchIds(ids);
        if(CollectionUtils.isNotEmpty(relationships)){
            relationships.stream().forEach(r -> {
                //分别取出来两个实体
                EntityHistoryModel entity1 = entityHistoryModelMap.get(r.getEntityId1());
                EntityHistoryModel entity2 = entityHistoryModelMap.get(r.getEntityId2());
                if(entity1 != null  &&  entity2 != null ){
                    RelationshipHistoryModel relationshipHistory = new RelationshipHistoryModel();
//                    relationshipHistory.setRelationshipId(r.getRelationshipId());
                    //插入的id值是历史表的id（uuid）
                    relationshipHistory.setEntityHistoryId1(entity1.getEntityHistoryid());
                    relationshipHistory.setEntityHistoryId2(entity2.getEntityHistoryid());
                    relationshipHistory.setRelationshipType(r.getRelationshipType());
                    baseMapper.insert(relationshipHistory);
                }
            });
        }


//        for (RelationshipModel relationship : relationships) {
//            // 检查是否存在重复数据
//            boolean isDuplicate = false;
//            for (RelationshipHistoryModel existingRelationship : existingRelationships) {
//                if (existingRelationship.getEntityHistoryId1().equals(relationship.getEntityId1()) &&
//                        existingRelationship.getEntityHistoryId2().equals(relationship.getEntityId2())) {
//                    // 存在重复数据，将标志设为true
//                    isDuplicate = true;
//                    break;
//                }
//            }
//            // 如果存在重复数据，则跳过该记录的添加
//            if (isDuplicate) {
//                continue;
//            }
//            // 添加关系数据到第二个表
//            RelationshipHistoryModel relationshipHistory = new RelationshipHistoryModel();
//            relationshipHistory.setRelationshipId(relationship.getRelationshipId());
//            relationshipHistory.setEntityHistoryId1(relationship.getEntityId1());
//            relationshipHistory.setEntityHistoryId2(relationship.getEntityId2());
//            relationshipHistory.setRelationshipType(relationship.getRelationshipType());
//            baseMapper.insert(relationshipHistory);
//        }


//
//        // 遍历第一个表的数据，将其添加到第二个表中
//        for (RelationshipModel relationship : relationships) {
//            RelationshipHistoryModel relationshipHistory = new RelationshipHistoryModel();
//            relationshipHistory.setRelationshipId(relationship.getRelationshipId());
//            relationshipHistory.setEntityHistoryId1(relationship.getEntityId1());
//            relationshipHistory.setEntityHistoryId2(relationship.getEntityId2());
//            relationshipHistory.setRelationshipType(relationship.getRelationshipType());
//            baseMapper.insert(relationshipHistory);
//        }
    }

    @Override
    public Map<String, Object> getAllRelationship(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
        List<EntityHistoryRelationship> entityHistoryRelationships = entityHistoryService.getEntityHistoryRelationships(entityId, keyWord);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityHistoryRelationship>().separatePageList(entityHistoryRelationships, requestParams);
        return requestParams;
    }

    @Override
    public void deleteByEntityHisId(String entityHisId) {
        List<RelationshipHistoryModel> relationshipHistoryList = findAllRelationshipHistoryByHisId(entityHisId);
        relationshipHistoryList.stream().forEach(relationshipHistoryModel -> {
            this.removeById(relationshipHistoryModel.getEntityHistoryId1());
        });
//        this.removeByIds(relationshipHistoryList);
    }

    /**
     * 通过历史id获取历史关联List
     *
     * @param entityHisId
     * @return
     */
    public List<RelationshipHistoryModel> findAllRelationshipHistoryByHisId(String entityHisId) {
        LambdaQueryWrapper<RelationshipHistoryModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(RelationshipHistoryModel::getEntityHistoryId1, entityHisId).or().eq(RelationshipHistoryModel::getEntityHistoryId2, entityHisId);
        return this.list(lambda);
    }
}
