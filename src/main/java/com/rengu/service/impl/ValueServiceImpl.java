package com.rengu.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.AttributeModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.AttributeMapper;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.HostInfoService;
import com.rengu.service.ValueService;
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
 * @ClassName ValueServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Service
public class ValueServiceImpl extends ServiceImpl<ValueMapper, ValueModel> implements ValueService {

    @Value("${mysql.valueSql}")
    private String valueSql;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HostInfoService hostInfoService;

    @Autowired
    private AttributeMapper attributeMapper;

    /**
     * 获取元数据属性列表
     *
     * @param hostInfo
     * @return
     */
    @Override
    public List<ValueAttribute> connect(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
        String sql = valueSql;
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<ValueModel> resultList = new ArrayList<>();
                List<AttributeModel> attributeModelList = new ArrayList<>();
                List<ValueAttribute> valueAttributes = new ArrayList<>();
                while (resultSet.next()) {
                    ValueModel entity = new ValueModel();
                    entity.setEntityId(resultSet.getString("entity_id"));
                    entity.setValue(resultSet.getString("value"));
                    entity.setValueId(resultSet.getString("value_id"));
                    entity.setAttributeId(resultSet.getString("attribute_id"));
                    resultList.add(entity);
                    AttributeModel attributeModel = new AttributeModel();
                    attributeModel.setAttributeId(resultSet.getString("attribute_id"));
                    attributeModel.setAttributeName(resultSet.getString("attribute_name"));
                    attributeModelList.add(attributeModel);
                    ValueAttribute valueAttribute = new ValueAttribute();
                    valueAttribute.setAttributeId(attributeModel.getAttributeId());
                    valueAttribute.setAttributeName(attributeModel.getAttributeName());
//                    valueAttribute.setAttribute(attributeModel);
                    valueAttribute.setValue(entity.getValue());
                    valueAttributes.add(valueAttribute);
                }
                if (resultList.size() > 0) {
                    redisUtils.set(RedisKeyPrefix.VALUE, resultList, 7200L);
                }
                if (attributeModelList.size() > 0) {
                    redisUtils.set(RedisKeyPrefix.ATTRIBUTE, attributeModelList, 7200L);
                }
                resultSet.close();
                statement.close();
                connection.close();
                return valueAttributes;
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * @param entityId
     * @param keyWord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> getAllValueInfo(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
        List<ValueAttribute> valueAttributes = hostInfoService.findValueAttributesByEntityId(entityId, keyWord);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<ValueAttribute>().separatePageList(valueAttributes, requestParams);
        return requestParams;
    }

    /**
     * 根据实体id查询属性值
     *
     * @param entityId
     * @param keyWord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> findValueInfoByEntityId(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
        if (StringUtils.isEmpty(entityId)) {
            return null;
        }
        List<ValueAttribute> valueAttributes = new ArrayList<>();
        List<ValueModel> valueModels = (List<ValueModel>) redisUtils.get(RedisKeyPrefix.VALUE);
        List<AttributeModel> attributeModels = (List<AttributeModel>) redisUtils.get(RedisKeyPrefix.ATTRIBUTE);

        valueModels.stream().forEach(valueModel -> {
            ValueAttribute valueAttribute = new ValueAttribute();
            if (valueModel.getEntityId().equals(entityId)) {
                List<AttributeModel> resultList = CollUtil.filter(attributeModels, attributeModel -> attributeModel.getAttributeId().equals(valueModel.getAttributeId()));
                resultList.stream().forEach(attributeModel -> {
                    valueAttribute.setValue(valueModel.getValue());
                    valueAttribute.setAttributeId(attributeModel.getAttributeId());
                    valueAttribute.setAttributeName(attributeModel.getAttributeName());
                });
                if (!StringUtils.isEmpty(keyWord)) {
                    if (valueAttribute.getValue().contains(keyWord) || valueAttribute.getAttributeName().contains(keyWord)) {
                        valueAttributes.add(valueAttribute);
                    }
                } else {
                    valueAttributes.add(valueAttribute);
                }
            }
        });
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<ValueAttribute>().separatePageList(valueAttributes, requestParams);
        return requestParams;
    }

    @Override
    public void deleteByEntityId(String entityId) {
        QueryWrapper<ValueModel> queryWrap = new QueryWrapper<>();
        queryWrap.eq("entity_id", entityId);
        List<ValueModel> valueModels = this.list(queryWrap);
        valueModels.stream().forEach(valueModel -> {
            if (valueModel.getAttributeId() != null) {
                attributeMapper.deleteById(valueModel.getAttributeId());
            }
            this.removeById(valueModel.getValue());
        });
//        this.getBaseMapper().deleteBatchIds(valueModels);
    }
}
