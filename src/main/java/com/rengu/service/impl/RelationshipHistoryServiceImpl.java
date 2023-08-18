package com.rengu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public void copyDataToRelationshipHistory(List<String> ids) {
        // 查询第一个表的数据
        List<RelationshipModel> relationships = relationshipMapper.selectBatchIds(ids);

        // 遍历第一个表的数据，将其添加到第二个表中
        for (RelationshipModel relationship : relationships) {
            RelationshipHistoryModel relationshipHistory = new RelationshipHistoryModel();
            relationshipHistory.setEntityHistoryId1(relationship.getEntityId1());
            relationshipHistory.setEntityHistoryId2(relationship.getEntityId2());
            relationshipHistory.setRelationshipType(relationship.getRelationshipType());
            baseMapper.insert(relationshipHistory);
        }
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
}
