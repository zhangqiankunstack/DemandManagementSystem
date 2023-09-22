package com.rengu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.*;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.service.*;
import com.rengu.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName RelationshipServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, RelationshipModel> implements RelationshipService {

    @Autowired
    private RelationshipMapper relationshipMapper;

    @Autowired
    private EntityMapper entityMapper;

    @Value("${mysql.relationshipSql}")
    private String relationshipSql;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HostInfoService hostInfoService;

    @Autowired
    private HostInfoServiceImpl hostInfoServiceImpl;


    @Override
    public List<EntityAndEntityVo> connect(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:kingbase8://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
//        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
        String sql = relationshipSql;
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<RelationshipModel> resultList = new ArrayList<>();
                List<EntityAndEntityVo> entityAndEntityVos = new ArrayList<>();
                while (resultSet.next()) {
                    RelationshipModel relationship = new RelationshipModel();
                    relationship.setRelationshipId(resultSet.getString("relationship_id"));
                    relationship.setRelationshipType(resultSet.getString("relationship_type"));
                    relationship.setEntityId1(resultSet.getString("entity_id1"));
                    relationship.setEntityId2(resultSet.getString("entity_id2"));
                    resultList.add(relationship);
                    EntityAndEntityVo entityAndEntityVo = new EntityAndEntityVo();
                    //todo:上移，无需多次获取
                    List<EntityModel> entityModels = (List<EntityModel>) redisUtils.get(RedisKeyPrefix.ENTITY);
                    if (entityModels.size() > 0 || entityModels != null) {
                        entityModels.stream().forEach(entityModel -> {
                            if (entityModel.getEntityId().equals(relationship.getEntityId1())) {
                                entityAndEntityVo.setEntity1_name(entityModel.getEntityName());
                            }
                            if (entityModel.getEntityId().equals(relationship.getEntityId2())) {
                                entityAndEntityVo.setEntity2_name(entityModel.getEntityName());
                            }
                        });
                    }
                    entityAndEntityVo.setRelationship_type(relationship.getRelationshipType());
                    entityAndEntityVos.add(entityAndEntityVo);
                }
                if (resultList.size() > 0) {
                    redisUtils.set(RedisKeyPrefix.RELATIONSHIP, resultList, 7200L);
                }

                resultSet.close();
                statement.close();
                connection.close();
                return entityAndEntityVos;
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getAllRelationship(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
//        List<EntityRelationship> entityRelationships = hostInfoService.getEntityRelationships(entityId, keyWord);
        List<EntityRelationship> entityRelationships = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<RelationshipModel> relationshipsByEntityIds = relationshipMapper.getRelationshipsByEntityIds(Collections.singletonList(entityId));
        Map<String, RelationshipModel> entityRelationshipMap = new HashMap<>();
        relationshipsByEntityIds.stream().forEach(r -> {
            if(entityId.equals(r.getEntityId1())){
                ids.add(r.getEntityId2());
                entityRelationshipMap.put(r.getEntityId2(), r);
            }else{
                ids.add(r.getEntityId1());
                entityRelationshipMap.put(r.getEntityId1(), r);
            }
        });
        if(!CollectionUtils.isEmpty(ids)){
            LambdaQueryWrapper<EntityModel> wrapper = new LambdaQueryWrapper<EntityModel>().in(EntityModel::getEntityId, ids);
            if(!StringUtils.isEmpty(keyWord)){
                wrapper.and(entityModelLambdaQueryWrapper -> entityModelLambdaQueryWrapper.eq(EntityModel::getEntityType, keyWord));
            }
             entityRelationships = entityMapper.selectList(wrapper)
                    .stream().map(e -> {
                        EntityRelationship entityRelationship = new EntityRelationship();
                        entityRelationship.setEntityId1(e.getEntityId());
                        entityRelationship.setEntityName1(e.getEntityName());
                        entityRelationship.setEntityType(e.getEntityType());
                        RelationshipModel relationshipModel = entityRelationshipMap.get(e.getEntityId());
                        entityRelationship.setRelationshipId(relationshipModel.getRelationshipId());
                        entityRelationship.setRelationshipType(relationshipModel.getRelationshipType());
                        return entityRelationship;
                    }).collect(Collectors.toList());
        }

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityRelationship>().separatePageList(entityRelationships, requestParams);
        return requestParams;
    }

    @Override
    public Map<String, Object> findRelationshipByEntityId(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
        if (StringUtils.isEmpty(entityId)) {
            return null;
        }
        List<RelationshipModel> relationshipModels = (List<RelationshipModel>) redisUtils.get(RedisKeyPrefix.RELATIONSHIP);
        List<EntityAndEntityVo> entityAndEntityVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relationshipModels)) {
            List<RelationshipModel> commonRelationList = CollUtil.filter(relationshipModels, hostInfoServiceImpl.getRelationshipByParams(entityId));

            List<EntityModel> entityModels = (List<EntityModel>) redisUtils.get(RedisKeyPrefix.ENTITY);

            commonRelationList.stream().forEach(relationshipModel -> {
                EntityAndEntityVo entityAndEntityVo = new EntityAndEntityVo();
                if (entityModels.size() > 0 || entityModels != null) {
                    entityModels.stream().forEachOrdered(entityModel -> {
//                        if (entityModel.getEntityId().equals(relationshipModel.getEntityId1())) {
//                            entityAndEntityVo.setEntity1_name(entityModel.getEntityName());
//                        }
//                        if (entityModel.getEntityId().equals(relationshipModel.getEntityId2())) {
//                            entityAndEntityVo.setEntity2_name(entityModel.getEntityName());
//                            entityAndEntityVo.setEntityType(entityModel.getEntityType());
//                            entityAndEntityVo.setRelationship_type(relationshipModel.getRelationshipType());
//                        }
                        if((!entityModel.getEntityId().equals(entityId))
                                && (entityModel.getEntityId().equals(relationshipModel.getEntityId1()) || entityModel.getEntityId().equals(relationshipModel.getEntityId2()))){
                            entityAndEntityVo.setEntity1_name(entityModel.getEntityName());
                            entityAndEntityVo.setEntityType(entityModel.getEntityType());
                            entityAndEntityVo.setRelationship_type(relationshipModel.getRelationshipType());
                        }

                    });
                    if (!StringUtils.isEmpty(keyWord)) {
                        if (entityAndEntityVo.getEntityType().equals(keyWord)) {
                            entityAndEntityVos.add(entityAndEntityVo);
                        }
                    } else {
                        entityAndEntityVos.add(entityAndEntityVo);
                    }
                }
            });
        }
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityAndEntityVo>().separatePageList(entityAndEntityVos, requestParams);
        return requestParams;
    }

    /**
     * 判断通过任务、能力类型的实体id查询关联关系是否存在
     *
     * @param capabilityId
     * @param missionId
     * @return
     */
    @Override
    public boolean getRelationshipByEntityIds(String capabilityId, String missionId) {
        RelationshipModel relationshipModel = null;
        QueryWrapper<RelationshipModel> query = new QueryWrapper<>();
        query.eq("entity_id1", capabilityId);
        query.eq("entity_id2", missionId);
        relationshipModel = this.getOne(query);
        if (relationshipModel == null) {
            query.clear();
            query.eq("entity_id1", missionId);
            query.eq("entity_id2", capabilityId);
            relationshipModel = this.getOne(query);
        }
        if (relationshipModel == null) {
            return false;
        } else {
            return true;
        }
    }
}
