package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.EntityModel;
import com.rengu.entity.vo.EntityHistoryRelationship;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttributeEntityVo;
import com.rengu.entity.vo.ValueAttributeHistory;
import com.rengu.mapper.EntityHistoryMapper;
import com.rengu.mapper.EntityMapper;
import com.rengu.service.EntityHistoryService;
import com.rengu.service.RelationshipHistoryService;
import com.rengu.service.RequirementService;
import com.rengu.service.ValueHistoryService;
import com.rengu.util.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EntityHistoryServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Service
public class EntityHistoryServiceImpl extends ServiceImpl<EntityHistoryMapper, EntityHistoryModel> implements EntityHistoryService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private EntityHistoryMapper entityHistoryMapper;

    @Autowired
    private RelationshipHistoryService relationshipHistoryService;

    @Autowired
    private ValueHistoryService valueHistoryService;

    @Override
    public void copyDataToEntityHistory(List<String> ids) {
        // 遍历传入的实体 ID 列表
        for (String entityId : ids) {
            // 查询该实体的历史记录数量
            int historyCount = baseMapper.selectCount(new QueryWrapper<EntityHistoryModel>()
                    .eq("entity_id", entityId));

            // 判断历史记录数量是否大于等于2，如果是则需要自增版本号
            if (historyCount >= 1) {
                // 获取该实体最新的历史记录
                EntityHistoryModel latestHistory = baseMapper.selectOne(new QueryWrapper<EntityHistoryModel>()
                        .eq("entity_id", entityId)
                        .orderByDesc("isTop")
                        .last("LIMIT 1"));

                // 版本号自增
                String version = incrementVersion(latestHistory.getVersion());

                // 创建新的历史记录
                EntityHistoryModel newHistory = new EntityHistoryModel();
                newHistory.setEntityId(latestHistory.getEntityId());
                newHistory.setVersion(version);
                newHistory.setEntityName(latestHistory.getEntityName());
                newHistory.setEntityType(latestHistory.getEntityType());
                newHistory.setIsTop(1);
                newHistory.setChanges("暂无变更内容");

                // 插入新的历史记录
                entityHistoryMapper.insert(newHistory);


                // 查询除了新插入的数据以外的所有具有相同 entityId 的数据列表
                QueryWrapper<EntityHistoryModel> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("entity_id", entityId);
                List<EntityHistoryModel> entityHistories = entityHistoryMapper.selectList(queryWrapper);

                // 遍历数据列表，将除了新插入的数据以外的其他数据的 isTop 设置为 0
                for (EntityHistoryModel entityHistory : entityHistories) {
                    if (!entityHistory.getEntityHistoryid().equals(newHistory.getEntityHistoryid())) {
                        entityHistory.setIsTop(0);
                        entityHistoryMapper.updateById(entityHistory);
                    }
                }

//                entityHistoryMapper.updateBatchIsTopById(entityHistories);

//                entityHistoryMapper.updateOtherIsTopToZero(newHistory.getEntityId(),0,newHistory.getEntityHistoryid());
            } else {
                // 查询实体数据
                EntityModel entity = entityMapper.selectById(entityId);

                // 版本号从 v.0.0.0.1 开始
                String version = "v.0.0.0.1";

                // 创建新的历史记录
                EntityHistoryModel entityHistory = new EntityHistoryModel();
                entityHistory.setEntityId(entity.getEntityId());
                entityHistory.setVersion(version);
                entityHistory.setEntityName(entity.getEntityName());
                entityHistory.setEntityType(entity.getEntityType());
                entityHistory.setIsTop(1);
                entityHistory.setChanges("暂无变更内容");

                // 插入新的历史记录
                baseMapper.insert(entityHistory);
            }
        }
    }

    @Override
    public void deleteByEntityId(String entityId) {
        LambdaQueryWrapper<EntityHistoryModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(EntityHistoryModel::getEntityId, entityId);
        this.list(lambda).stream().forEach(entityHistory -> {
            deleteByEntityHisId(entityHistory.getEntityId());
        });
    }

    //
    public void deleteByEntityHisId(String entityHisId) {
        //删除历史关联关系
        relationshipHistoryService.deleteByEntityHisId(entityHisId);
        //删除value历史
        valueHistoryService.deleteByEntityHisId(entityHisId);
        //删除历史
        this.removeById(entityHisId);
    }



    @Override
    public Map<String, Object> getAllEntity(String keyWord, Integer pageNumber, Integer pageSize) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        QueryWrapper<EntityHistoryModel> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("entity_type", keyWord);
        }
        queryWrapper.eq("isTop",1);
        List<EntityHistoryModel> entities = this.list(queryWrapper);
        return new ListPageUtil().separatePageList(entities, requestParams);

    }



    private String incrementVersion(String version) {
        // 将版本号的数字部分提取出来
        String[] parts = version.split("\\.");
        int lastPart = Integer.parseInt(parts[parts.length - 1]);

        // 数字自增
        lastPart++;

        // 更新版本号
        parts[parts.length - 1] = String.valueOf(lastPart);

        // 构造新的版本号并返回
        return String.join(".", parts);
    }


    @Override
    public List<EntityHistoryRelationship> getEntityHistoryRelationships(String entityId, String keyWord) {
        List<EntityHistoryRelationship> list = entityHistoryMapper.getEntityHistoryRelationships(entityId, keyWord);
        return list;
    }

    @Override
    public Map<String, Object> getAllNowEntityHistory(String entityId,String keyWord, Integer pageNumber, Integer pageSize) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        QueryWrapper<EntityHistoryModel> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("entity_name", keyWord);
        }
        if (!StringUtils.isEmpty(entityId)) {
            queryWrapper.eq("entity_id", entityId);
        }
        List<EntityHistoryModel> entities = this.list(queryWrapper);
        return new ListPageUtil().separatePageList(entities, requestParams);

    }


    /**
     * 版本比对
     * @param entityHistoryId
     * @return
     */
    @Override
    public List<ValueAttributeEntityVo> findValueByEntityHistoryId(String entityHistoryId) {


        return entityHistoryMapper.getValueAttribute(entityHistoryId);




    }

    @Override
    public void recover(String id) {
        EntityHistoryModel byId = entityHistoryMapper.selectById(id);
        EntityHistoryModel entityHistoryModel = new EntityHistoryModel();
        entityHistoryModel.setEntityId(byId.getEntityId());
        entityHistoryModel.setEntityType(byId.getEntityType());
        entityHistoryModel.setEntityHistoryid(byId.getEntityHistoryid());
        entityHistoryModel.setEntityName(byId.getEntityName());

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = currentTime.format(formatter);
        entityHistoryModel.setChanges("恢复为当前版本,修改时间为"+currentTimeString);

        entityHistoryModel.setVersion(byId.getVersion());
        entityHistoryModel.setIsTop(1);
        entityHistoryMapper.updateOtherIsTop(byId.getEntityId(),byId.getEntityHistoryid());
        entityHistoryMapper.updateOtherChanges(byId.getEntityId(),byId.getEntityHistoryid());
        entityHistoryMapper.updateById(entityHistoryModel);
        entityHistoryMapper.updateOtherIsTopToZero(entityHistoryModel.getEntityId(),0,entityHistoryModel.getEntityHistoryid());


    }





    @Override
    public List<EntityHistoryRelationship> getRelatedEntities(String entityHistoryId, String entityType) {


        List<EntityHistoryRelationship> relationshipEntitiesByEntityHistoryId = entityHistoryMapper.getRelationshipEntitiesByEntityHistoryId(entityHistoryId, entityType);
        return relationshipEntitiesByEntityHistoryId;





//        QueryWrapper<EntityHistoryModel> queryWrapper = new QueryWrapper<>();
//        queryWrapper.inSql("entity_id", String.format("SELECT entity_id FROM entity_history WHERE entity_historyid = '%s'", entityHistoryId))
//                .or()
//                .inSql("entity_id", String.format("SELECT entity_history_id1 FROM relationship_history WHERE entity_history_id1 IN (SELECT entity_id FROM entity_history WHERE entity_historyid = '%s')", entityHistoryId))
//                .or()
//                .inSql("entity_id", String.format("SELECT entity_history_id2 FROM relationship_history WHERE entity_history_id2 IN (SELECT entity_id FROM entity_history WHERE entity_historyid = '%s')", entityHistoryId));
//
//        if (entityType != null && !entityType.isEmpty()) {
//            queryWrapper.like("entity_type", entityType);
//        }
//
//        return entityHistoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<EntityHistoryModel> getEntityHistoryByEntityId(String entityId, String excludedEntityHistoryId) {
        return entityHistoryMapper.getEntityHistoryByEntityId(entityId, excludedEntityHistoryId);
    }


}
