package com.rengu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.ValueService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取元数据属性列表
     *
     * @param hostInfo
     * @return
     */
    @Override
    public List<ValueModel> connect(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getNewDatabase() + "?serverTimezone=GMT";
        String sql = valueSql;
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<ValueModel> resultList = new ArrayList<>();
                while (resultSet.next()) {
                    ValueModel entity = new ValueModel();
                    entity.setEntityId(resultSet.getString("entity_id"));
                    entity.setValue(resultSet.getString("value"));
                    entity.setValueId(resultSet.getString("value_id"));
                    entity.setAttributeId(resultSet.getString("attribute_id"));
                    resultList.add(entity);
                }
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
}
