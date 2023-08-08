package com.rengu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.HostInfoMapper;
import com.rengu.mapper.ValueAttributeMapper;
import com.rengu.service.*;
import com.rengu.util.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HostInfoServiceImpl
 * @Description 数据库表服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@Service
public class HostInfoServiceImpl extends ServiceImpl<HostInfoMapper, HostInfoModel> implements HostInfoService {

    @Value("${mysql.sql}")
    private String sql;

    @Autowired
    private EntityService entityService;
    @Autowired
    private ValueAttributeMapper valueAttributeMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private ValueService valueService;
    @Autowired
    private AttributeService attributeService;

    @Override
    public boolean databaseTest(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getNewDatabase() + "?serverTimezone=GMT";
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                System.out.println("Database connection successful!");

                // 执行其他数据库操作
                // ...
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return false;
    }


    /**
     * 连接后查询
     *
     * @param hostInfo
     * @return
     */
    @Override
    public List<EntityModel> connect(HostInfoModel hostInfo) {
        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getNewDatabase() + "?serverTimezone=GMT";
        String sql1 = sql;
        try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
            if (connection != null) {
                System.out.println("连接成功");
                // 创建 Statement 对象
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql1);
                List<EntityModel> resultList = new ArrayList<>();
                while (resultSet.next()) {
                    EntityModel entity = new EntityModel();
                    entity.setEntityId(resultSet.getString("entity_id"));
                    entity.setEntityName(resultSet.getString("entity_name"));
                    entity.setEntityType(resultSet.getString("entity_type"));
                    resultList.add(entity);
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
        return null;
    }

    /**
     * 查询数据
     *
     * @param entityId
     * @return
     */
    @Override
    public List<ValueAttribute> findValueAttributesByEntityId(String entityId) {
        List<ValueAttribute> valueAttributeList = valueAttributeMapper.findValueAttributesByEntityId(entityId);

        return valueAttributeList;

    }

    @Override
    public List<EntityRelationship> getEntityRelationships(String entityId1) {
        List<EntityRelationship> list = entityMapper.getEntityRelationships(entityId1);

        boolean entityRelationship = redisTemplateUtil.lSet("entityRelationship", list, 6000);

        return list;

    }

    @Override
    public void insertAll(String id) {
        List<EntityModel> list = entityService.list();
        for (EntityModel entityModel : list) {
            if (entityModel.getEntityId() == id) {
                entityService.save(entityModel);

            }
        }
        List<RelationshipModel> list1 = relationshipService.list();
        for (RelationshipModel relationshipModel : list1) {
            if (relationshipModel.getEntityId1() == id) {
                relationshipService.save(relationshipModel);

            }
        }
        List<ValueModel> list2 = valueService.list();
        for (ValueModel valueModel : list2) {
            if (valueModel.getEntityId() == id) {
                valueService.save(valueModel);

            }
        }
        List<AttributeModel> list3 = attributeService.list();
        for (AttributeModel attributeModel : list3) {
            if (attributeModel.getAttributeId() == id) {
                attributeService.save(attributeModel);
//这里的添加不符合
            }
        }

    }

    public Integer saveOrUpdateDbInfo(HostInfoModel dbInfo) {
        if (StringUtils.isEmpty(dbInfo.getId())) {
            return baseMapper.insert(dbInfo);
        } else {
            return baseMapper.updateById(dbInfo);
        }
    }

    public Integer deletedDbInfoById(String dbInfoId) {
        if (StringUtils.isEmpty(dbInfoId)) {
            return 0;
        }
        return baseMapper.deleteById(dbInfoId);
    }

    //通过id获取数据源
    public HostInfoModel getDbInfoById(String dbInfoId) {
        return baseMapper.selectById(dbInfoId);
    }

    /**
     * 导入任务清单文件
     *
     * @param multipartFileList
     * @return
     */

    public Object importTaskFiles1(MultipartFile multipartFileList) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(multipartFileList.getInputStream());

            //解析XML内容
            analyze(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析xml
     *
     * @param document
     * @return
     */
    public Object analyze(Document document) {
        // 获取根元素
        Element rootElement = document.getDocumentElement();
        switch (rootElement.toString()) {
            case "missionInventory":
                //todo:任务
                break;
            case "activityInventory":
                //todo:活动
                break;
            case "capabilityInventory":
                //todo:作战
                break;
            case "equipmentInventory":
                //todo:装备
                break;
            default:
                break;
        }
        return null;
    }


    @Override
    public Object importTaskFiles(MultipartFile multipartFileList) {
        try {
            // 创建DocumentBuilder对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
            Document doc = builder.parse(multipartFileList.getInputStream());
            //获取根节点
            Element rootElement = doc.getDocumentElement();
            // 获取所有book节点的集合
            NodeList bookList = doc.getElementsByTagName("book");
            System.out.println("一共有" + bookList.getLength() + "本书");
            //遍历每一个book节点
            for (int i = 0; i < bookList.getLength(); i++) {
                System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
                //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
                Node book = bookList.item(i);
                // 获取book节点的所有属性集合
                NamedNodeMap attrs = book.getAttributes();
                System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                // 遍历book的属性
                for (int j = 0; j < attrs.getLength(); j++) {
                    //通过item(index)方法获取book节点的某一个属性
                    Node attr = attrs.item(j);
                    //获取属性名
                    System.out.print("属性名：" + attr.getNodeName());
                    //获取属性值
                    System.out.println("--属性值" + attr.getNodeValue());
                }
                //解析book节点的子节点
                NodeList childNodes = book.getChildNodes();
                //遍历childNodes获取每个节点的节点名和节点值
                System.out.println("第" + (i + 1) + "本书共有" + childNodes.getLength() + "个子节点");
                for (int k = 0; k < childNodes.getLength(); k++) {
                    // 区分出text类型的node以及element类型的node
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        //获取了element类型节点的节点名
                        System.out.print("第" + (k + 1) + "个节点的节点名：" + childNodes.item(k).getNodeName());
                        //获取了element类型节点的节点值
                        System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
                        //System.out.println("--节点值是：" + childNodes.item(k).getTextContent());
                    }
                }
                System.out.println("======================结束遍历第" + (i + 1) + "本书的内容=================");
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}