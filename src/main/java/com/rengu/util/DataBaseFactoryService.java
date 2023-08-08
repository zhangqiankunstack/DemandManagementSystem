package com.rengu.util;

import com.rengu.entity.HostInfoModel;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @program Effectiveness evaluation
 * @Author Miracle ZHQK
 * @Description:数据库对象工厂类
 * @Date 2020/12/16 13:33
 * @Version 1.0
 */

@Service
public class DataBaseFactoryService {
    private static DataBaseFactoryService dataBaseFactoryService = null;
    public Map<String, BasicDataSource> dbinfoMap = new HashMap<>();

    public DataBaseFactoryService() {

    }

    //获取实例化
    public static DataBaseFactoryService getDbFactory() {
        //获取DataBaseFactory实例对象
        if (dataBaseFactoryService == null) {
//            synchronized (DataConfigurationService.class) {
            if (dataBaseFactoryService == null) {
                dataBaseFactoryService = new DataBaseFactoryService();
//                }
            }
        }
        return dataBaseFactoryService;
    }

    //判断是否连接成功（数据库测试连接）
    public boolean testConnect(HostInfoModel dbInfo) {
        Connection connection = getConnection(dbInfo);
        System.out.println("数据库连接信息：" + connection);
        if (connection == null) {
            return false;
        } else {
            Statement statement = null;
            ResultSet rs = null;
            boolean var6;
            try {
                statement = connection.createStatement();
                String type = dbInfo.getDbType();
                String sql = null;
                switch (type) {
                    case "MySql":
                        sql = "select 1";
                        break;
                    case "Oracle":
                        sql = "select 1 from dual";
                        break;
                    case "SQLServer":
                        sql = "SELECT 'x'";
                        break;
                    case "PostgreSQL":
                        sql = "select version()";
                        break;
                }
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                }
                boolean var5 = true;
                rs.close();
                return var5;
            } catch (SQLException e) {
                e.printStackTrace();
                var6 = false;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException var23) {
                        var23.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException var22) {
                        var22.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException var21) {
                        var21.printStackTrace();
                    }
                }

            }
            return var6;
        }
    }

    //连接数据库
    public Connection getConnection(HostInfoModel dbInfo) {
        Connection connection = null;
        if (!dbinfoMap.containsKey(dbInfo.getId())) {
            //添加数据源
            addDataSource(dbInfo);
        }
        try {
            connection = dbinfoMap.get(dbInfo.getId()).getConnection();
        } catch (Exception e) {
            removeDataSource(dbInfo);
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 添加数据源
     *
     * @param dbinfo
     */
    public void addDataSource(HostInfoModel dbinfo) {
        synchronized (DataBaseFactoryService.class) {
            //再次判断是否包含
            if (!dbinfoMap.containsKey(dbinfo.getId())) {
                dbinfoMap.put(dbinfo.getId(), createDataSource(dbinfo));
            }
        }
    }

    //创建数据源
    public BasicDataSource createDataSource(HostInfoModel dbInfo) {
        String dataBase = getBasicData(dbInfo);
        getDataSource(dataBase, dbInfo);
        return getDataSource(dataBase, dbInfo);
    }

    public String getBasicData(HostInfoModel dbInfo) {
        String type = dbInfo.getDbType();
        String dataBase = null;
        switch (type) {
            case "MySql":
                dataBase = "com.mysql.jdbc.Driver";
                break;
            case "Oracle":
                dataBase = "oracle.jdbc.driver.OracleDriver";
                break;
            case "SQLServer":
                dataBase = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
            case "Postgresql":
                dataBase = "org.postgresql.Driver";
                break;
        }
        return dataBase;
    }

    public BasicDataSource getDataSource(String dataBase, HostInfoModel dbInfo) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dataBase);
        StringBuffer Url = new StringBuffer();
        //拼接datasource.url
        Url.append("jdbc:");
        Url.append(dbInfo.getDbType());
        Url.append("://");
        Url.append(dbInfo.getHostIp());
        Url.append(":");
        Url.append(dbInfo.getPort());
        Url.append("/");
        Url.append(dbInfo.getDbName());
        Url.append("?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        System.out.println("拼接路径：" + Url.toString());
        dataSource.setUrl(Url.toString());
        dataSource.setUsername(dbInfo.getUsername());
        dataSource.setPassword(dbInfo.getPassword());
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(40);
        dataSource.setMaxWait(1000L);
        return dataSource;
    }

    //删除数据源
    private void removeDataSource(HostInfoModel dbInfo) {
        BasicDataSource basicDataSource = dbinfoMap.get(dbInfo.getId());
        if (basicDataSource != null) {
            try {
                basicDataSource.close();
                dbinfoMap.remove(dbInfo.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
