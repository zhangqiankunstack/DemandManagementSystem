package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.mapper.RelationshipHistoryMapper;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.service.RelationshipHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void copyDataToRelationshipHistory(List<String> ids) {
        // 查询第一个表的数据
        List<RelationshipModel> relationships = relationshipMapper.selectBatchIds(ids);

        // 遍历第一个表的数据，将其添加到第二个表中
        for (RelationshipModel relationship : relationships) {
            RelationshipHistoryModel relationshipHistory = new RelationshipHistoryModel();
            relationshipHistory.setRelationshipId(relationship.getRelationshipId());
            relationshipHistory.setEntityHistoryId1(relationship.getEntityId1());
            relationshipHistory.setEntityHistoryId2(relationship.getEntityId2());
            relationshipHistory.setRelationshipType(relationship.getRelationshipType());
            baseMapper.insert(relationshipHistory);
        }
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
