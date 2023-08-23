package com.rengu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import java.sql.ResultSet;
import java.util.ArrayList;

@Component
public class DatabaseUtils {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CHARSET = "UTF-8";
    private static String DB_URL = "";
    private static String DB_USERNAME = "";
    private static String DB_PASSWORD = "";

    private static String MY_SQL_IP = "";

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;



    @PostConstruct
    public void getServerParam(){
        DB_USERNAME = this.userName;
        DB_PASSWORD = this.password;
        MY_SQL_IP = url;
    }

    public static void getMySQLParam() {
        String result = null;
        try {
            int index = MY_SQL_IP.indexOf('?');

            if (index != -1) {
                // 截取问号之前的字符串
                result = MY_SQL_IP.substring(0, index);
                System.out.println("截取的结果：" + result);
            }
            Class.forName(JDBC_DRIVER);
        } catch (Exception exx) {
            exx.printStackTrace();
        }
        if (result.contains(":")) {
            DB_URL = result;
        }
    }

    public static void insertEntity(String entityId, String entityName, String entityType) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }

        deleteAllEntityId(entityId);

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            String insertEntityQuery = "INSERT INTO entity (entity_id, entity_name, entity_type) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(insertEntityQuery);
            stmt.setString(1, entityId);
            stmt.setString(2, entityName);
            stmt.setString(3, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
    }

    private static void deleteAllEntityId(String entityId) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
        ;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);


            String deleteEntityQuery = "DELETE FROM entity WHERE entity_id = ?";
            stmt = conn.prepareStatement(deleteEntityQuery);
            stmt.setString(1, entityId);
            stmt.executeUpdate();
            String deleteValueQuery = "DELETE FROM value WHERE entity_id = ?";
            stmt = conn.prepareStatement(deleteValueQuery);
            stmt.setString(1, entityId);
            stmt.executeUpdate();
            String deleteRelationshipQuery = "DELETE FROM relationship WHERE entity_id1 = ? or entity_id2 = ?";
            stmt = conn.prepareStatement(deleteRelationshipQuery);
            stmt.setString(1, entityId);
            stmt.setString(2, entityId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteAll() {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
        ;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);


            String deleteEntityQuery = "DELETE FROM entity";
            stmt = conn.prepareStatement(deleteEntityQuery);
            stmt.executeUpdate();
            String deleteValueQuery = "DELETE FROM value";
            stmt = conn.prepareStatement(deleteValueQuery);
            stmt.executeUpdate();
            String deleteRelationshipQuery = "DELETE FROM relationship";
            stmt = conn.prepareStatement(deleteRelationshipQuery);
            stmt.executeUpdate();
            String deleteAttributeQuery = "DELETE FROM attribute";
            stmt = conn.prepareStatement(deleteAttributeQuery);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertRelationship(String relationshipId, String relationshipType, String entityId1,
                                          String entityId2) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到relationship表中
            String insertRelationshipQuery = "INSERT INTO relationship (relationship_id, entity_id1, entity_id2, relationship_type) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertRelationshipQuery);
            stmt.setString(1, relationshipId);
            stmt.setString(2, entityId1);
            stmt.setString(3, entityId2);
            stmt.setString(4, relationshipType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
    }

    public static String selectAttributeId(String attributeName) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到arrribute表中
            String selectAttributeQuery = "SELECT * FROM attribute WHERE attribute_name = ?";
            stmt = conn.prepareStatement(selectAttributeQuery);
            stmt.setString(1, attributeName);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String attribute_id = resultSet.getString("attribute_id");
                return attribute_id;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
        return null;
    }

    public static void insertAttribute(String attributeId, String attributeName) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到arrribute表中
            String insertAttributeQuery = "INSERT INTO attribute (attribute_id, attribute_name) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertAttributeQuery);
            stmt.setString(1, attributeId);
            stmt.setString(2, attributeName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
    }

    public static void insertValue(String valueId, String entityId, String attributeId, String value) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到value表中
            String insertValueQuery = "INSERT INTO value (value_id, entity_id, attribute_id, value) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertValueQuery);
            stmt.setString(1, valueId);
            stmt.setString(2, entityId);
            stmt.setString(3, attributeId);
            stmt.setString(4, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
    }

//	public static List<Requirment> getAllRequirments() {
//		if(DB_URL.equals("")){
//			getMySQLParam();
//		}
//		List<Requirment> requirments = new ArrayList<>();
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		try {
//			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//			// 查询实体
//			String valueQuery = "SELECT entity_name, entity_id, entity_type FROM entity";
//			stmt = conn.prepareStatement(valueQuery);
//			ResultSet resultSet = stmt.executeQuery();
//
//			while (resultSet.next()) {
//				String entityname = resultSet.getString("entity_name");
//				String entityid = resultSet.getString("entity_id");
//				String entitytype = resultSet.getString("entity_type");
//
//				Requirment requirment = new Requirment();// 每次都创建一个新对象存值
//				requirment.setEntityid(entityid);
//				requirment.setEntityname(entityname);
//				requirment.setEntitytype(entitytype);
//				requirments.add(requirment);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closePreparedStatement(stmt);
//			closeConnection(conn);
//		}
//		return requirments;
//	}

//	public static List<Requirment> getAllInformations() {
//		if(DB_URL.equals("")){
//			getMySQLParam();
//		}
//		List<Requirment> requirments = new ArrayList<>();
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		try {
//			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//			// 查询实体
//			String valueQuery = "SELECT	v.*,e.entity_name,e.entity_type,a.attribute_name " +
//								"FROM VALUE v,entity e,attribute a " +
//								"WHERE v.entity_id = e.entity_id " +
//								"AND v.attribute_id = a.attribute_id";
//			stmt = conn.prepareStatement(valueQuery);
//			ResultSet resultSet = stmt.executeQuery();
//
//			while (resultSet.next()) {
//				String value_id = resultSet.getString("value_id");
//				String value = resultSet.getString("value");
//				String entityid = resultSet.getString("entity_id");
//				String entitytype = resultSet.getString("entity_type");
//				String entityname= resultSet.getString("entity_name");
//				String attribute_id= resultSet.getString("attribute_id");
//				String attributename= resultSet.getString("attribute_name");
//
//				Requirment requirment = new Requirment(value_id,value,entityid,entitytype,entityname,attribute_id,attributename);// 每次都创建一个新对象存值
//				requirments.add(requirment);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closePreparedStatement(stmt);
//			closeConnection(conn);
//		}
//		return requirments;
//	}

    public static String selectRelationshipForSystem(String systemId) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到arrribute表中
            String selectAttributeQuery = "SELECT * FROM entity e,relationship r WHERE e.entity_id=r.entity_id1 AND entity_type='metanode' AND r.entity_id2= ?";

            stmt = conn.prepareStatement(selectAttributeQuery);
            stmt.setString(1, systemId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String metaNodeId = resultSet.getString("entity_id");
                return metaNodeId;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
        return null;
    }

    private static void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> selectId() {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            String insertEntityQuery = "select entity_id from entity";
            stmt = conn.prepareStatement(insertEntityQuery);
            ResultSet resultSet = stmt.executeQuery();

            // 处理查询结果
            List<String> ids = new ArrayList<>();
            while (resultSet.next()) {
                // 获取列值，假设表中有列名为 "column1" 和 "column2"
                String id = resultSet.getString("entity_id");
                ids.add(id);
            }
            return ids;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
        return null;
    }

    public static String selectRelatedIds(String entityId1, String entityId2) {
        if (DB_URL.equals("")) {
            getMySQLParam();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // 设置连接字符集编码为UTF-8,防止插入数据出现中文乱码
            String connectionString = DB_URL + "?useUnicode=true&characterEncoding=" + DB_CHARSET + "&serverTimezone=GMT%2B8";
            conn = DriverManager.getConnection(connectionString, DB_USERNAME, DB_PASSWORD);
            // 插入到arrribute表中
            String selectAttributeQuery = "SELECT * FROM relationship WHERE (entity_id1 = ? and entity_id2 = ?) or (entity_id1 = ? and entity_id2 = ?)";
            stmt = conn.prepareStatement(selectAttributeQuery);
            stmt.setString(1, entityId1);
            stmt.setString(2, entityId2);
            stmt.setString(3, entityId2);
            stmt.setString(4, entityId1);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String relationship_id = resultSet.getString("relationship_id");
                return relationship_id;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
            closeConnection(conn);
        }
        return null;
    }
}
