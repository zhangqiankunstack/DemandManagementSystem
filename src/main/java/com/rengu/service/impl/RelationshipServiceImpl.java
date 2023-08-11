package com.rengu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.service.EntityService;
import com.rengu.service.HostInfoService;
import com.rengu.service.RelationshipService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.RedisKeyPrefix;
import com.rengu.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RelationshipServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, RelationshipModel> implements RelationshipService {

    @Value("${mysql.relationshipSql}")
    private String relationshipSql;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HostInfoService hostInfoService;


    @Override
    public List<EntityAndEntityVo> connect(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
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
        List<EntityRelationship> entityRelationships = hostInfoService.getEntityRelationships(entityId, keyWord);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityRelationship>().separatePageList(entityRelationships, requestParams);
        return requestParams;
    }
}
