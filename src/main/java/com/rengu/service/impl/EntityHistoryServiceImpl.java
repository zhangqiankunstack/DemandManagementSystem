package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.EntityModel;
import com.rengu.mapper.EntityHistoryMapper;
import com.rengu.mapper.EntityMapper;
import com.rengu.service.EntityHistoryService;
import com.rengu.service.RelationshipHistoryService;
import com.rengu.service.RequirementService;
import com.rengu.service.ValueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            if (historyCount >= 2) {
                // 获取该实体最新的历史记录
                EntityHistoryModel latestHistory = baseMapper.selectOne(new QueryWrapper<EntityHistoryModel>()
                        .eq("entity_id", entityId)
                        .orderByDesc("id")
                        .last("LIMIT 1"));

                // 版本号自增
                String version = incrementVersion(latestHistory.getVersion());

                // 创建新的历史记录
                EntityHistoryModel newHistory = new EntityHistoryModel();
                newHistory.setEntityId(latestHistory.getEntityId());
                newHistory.setVersion(version);
                newHistory.setEntityName(latestHistory.getEntityName());
                newHistory.setEntityType(latestHistory.getEntityType());

                // 插入新的历史记录
                baseMapper.insert(newHistory);
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

}
