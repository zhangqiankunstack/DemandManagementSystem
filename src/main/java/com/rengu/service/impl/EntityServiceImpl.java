package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.mapper.EntityMapper;
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
                relationshipService.getRelationshnipByEntityIds(capability.getEntityId(),mission.getEntityId());
            });
        });

        return null;
    }

    public static List<RelationshipModel> calculateCartesianProduct(List<String> list1, List<String> list2) {
        return list1.stream().flatMap(id1 -> list2.stream().map(id2 -> new RelationshipModel("", id1, id2, ""))).collect(Collectors.toList());
    }

}
