package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.entity.ValueModel;
import com.rengu.entity.vo.EntityQueryVo;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.EntityService;
import com.rengu.service.RelationshipService;
import com.rengu.util.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName EntityServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Service
public class EntityServiceImpl extends ServiceImpl<EntityMapper, EntityModel> implements EntityService {
    @Value("${mysql.entitySql}")
    private String entitySql;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private RelationshipMapper relationshipMapper;

    /**
     * 连接后查询
     *
     * @param hostInfo
     * @param keyWord
     * @return
     */
    @Override
    public List<EntityModel> connect(HostInfoModel hostInfo, String keyWord) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
        String sql = entitySql;
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<EntityModel> resultList = new ArrayList<>();
                while (resultSet.next()) {
                    EntityModel entity = new EntityModel();
                    entity.setEntityId(resultSet.getString("entity_id"));
                    entity.setEntityName(resultSet.getString("entity_name"));
                    entity.setEntityType(resultSet.getString("entity_type"));
                    if (!StringUtils.isEmpty(keyWord)) {
                        if (entity.getEntityType().equals(keyWord)) {
                            resultList.add(entity);
                        }
                    } else {
                        resultList.add(entity);
                    }
                }
                //todo:后期修改
//                resultList = ResultSetToListConverter.convertToList(resultSet, EntityModel.class);
                resultSet.close();
                statement.close();
                connection.close();
                return resultList;
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getAllEntity(String keyWord, Integer pageNumber, Integer pageSize) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        QueryWrapper<EntityModel> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("entity_type", keyWord);
        }
        List<EntityModel> entities = this.list(queryWrapper);
        return new ListPageUtil().separatePageList(entities, requestParams);
    }

    @Override
    public Object missionAndCapabilityTrace() {
        QueryWrapper<EntityModel> queryWrap = new QueryWrapper<>();
        queryWrap.eq("entity_type", "mission");
        List<EntityModel> missions = this.list(queryWrap);
        queryWrap.clear();
        queryWrap.eq("entity_type", "capability");
        List<EntityModel> capabilities = this.list(queryWrap);
//        List<String> missionIds = missions.stream().map(EntityModel::getEntityId).collect(Collectors.toList());//行
//        List<String> capabilityIds = capabilities.stream().map(EntityModel::getEntityId).collect(Collectors.toList());//列
        // 获取两组ids的两两组合
//        List<RelationshipModel> relationshipModels = calculateCartesianProduct(missionIds, capabilityIds);
        List<List<String>> listList = new ArrayList<>();
        capabilities.stream().forEach(capability -> {
            missions.stream().forEach(mission -> {
                relationshipService.getRelationshnipByEntityIds(capability.getEntityId(), mission.getEntityId());
            });
        });

        return null;
    }

    public static List<RelationshipModel> calculateCartesianProduct(List<String> list1, List<String> list2) {
        return list1.stream().flatMap(id1 -> list2.stream().map(id2 -> new RelationshipModel("", id1, id2, ""))).collect(Collectors.toList());
    }

    @Override
    public EntityQueryVo queryEntities(List<String> entityIdList) {

        // 查询 attributeId 和 valueId
        QueryWrapper<ValueModel> valueQueryWrapper = new QueryWrapper<>();
        valueQueryWrapper.in("entity_id", entityIdList);
        List<ValueModel> valueList = valueMapper.selectList(valueQueryWrapper);

        EntityQueryVo entityQueryVo = new EntityQueryVo();
        List<String> attributeIdList = new ArrayList<>();
        List<String> valueIdList = new ArrayList<>();
        entityQueryVo.setAttributeIdList(attributeIdList);
        entityQueryVo.setValueIdList(valueIdList);

        for (ValueModel value : valueList) {
            attributeIdList.add(value.getAttributeId());
            valueIdList.add(value.getValueId());
        }

        // 查询 relationshipId
        QueryWrapper<RelationshipModel> relationshipQueryWrapper = new QueryWrapper<>();
        relationshipQueryWrapper.in("entity_id1", entityIdList).or().in("entity_id2", entityIdList);
        List<RelationshipModel> relationshipList = relationshipMapper.selectList(relationshipQueryWrapper);


        List<String> relationshipIdList = new ArrayList<>();
        entityQueryVo.setRelationshipIdList(relationshipIdList);
//        List<String> relationshipIdList = entityQueryVo.getRelationshipIdList();

        for (RelationshipModel relationship : relationshipList) {
            relationshipIdList.add(relationship.getRelationshipId());
        }
        return entityQueryVo;
    }
}
