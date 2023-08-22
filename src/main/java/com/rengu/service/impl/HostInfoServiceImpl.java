package com.rengu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.*;
import com.rengu.mapper.*;
import com.rengu.service.*;
import com.rengu.util.*;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName HostInfoServiceImpl
 * @Description 数据库表服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@Service
public class HostInfoServiceImpl extends ServiceImpl<HostInfoMapper, HostInfoModel> implements HostInfoService {

    @Autowired
    private EntityService entityService;
    @Autowired
    private ValueAttributeMapper valueAttributeMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private ValueService valueService;
    @Autowired
    private AttributeService attributeService;

    /**
     * 查询数据
     *
     * @param entityId
     * @return
     */
    @Override
    public List<ValueAttribute> findValueAttributesByEntityId(String entityId, String keyWord) {
        List<ValueAttribute> valueAttributeList = valueAttributeMapper.findValueAttributesByEntityId(entityId, keyWord);
        return valueAttributeList;
    }

    @Override
    public List<EntityRelationship> getEntityRelationships(String entityId, String keyWord) {
        List<EntityRelationship> list = entityMapper.getEntityRelationships(entityId, keyWord);
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

    @Override
    public boolean saveOrUpdateDbInfo(HostInfoModel dbInfo) {
        return this.saveOrUpdate(dbInfo);
    }

    @Override
    public Integer deletedDbInfoById(String dbInfoId) {
        if (StringUtils.isEmpty(dbInfoId)) {
            return 0;
        }
        return baseMapper.deleteById(dbInfoId);
    }

    //通过id获取数据源
    @Override
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

    /**
     * 保存元数据实体、关联关系、属性
     *
     * @param entity
     * @param relationship
     * @param attributeModel
     * @param entityIds
     * @return
     */
    @Override
    public List<EntityModel> saveMetadata(List<EntityModel> entity, List<RelationshipModel> relationship, List<AttributeModel> attributeModel, List<ValueModel> valueModels, List<String> entityIds) {
        List<EntityModel> commonEntityList = entity.stream().filter(entityModel -> entityIds.contains(entityModel.getEntityId())).collect(Collectors.toList());
        //保存实体表

//        commonEntityList.stream().forEach(entityModel -> {
        for (EntityModel entityModel : commonEntityList) {
            entityService.saveOrUpdate(entityModel);
            List<RelationshipModel> commonRelationList = CollUtil.filter(relationship, getRelationshipByParams(entityModel.getEntityId()));
            relationshipService.saveOrUpdateBatch(commonRelationList);
            List<ValueModel> commonValues = CollUtil.filter(valueModels, getValueModelByParams(entityModel.getEntityId()));
            valueService.saveOrUpdateBatch(commonValues);
            List<AttributeModel> commonAttributeModels = CollUtil.filter(attributeModel, getByParams(commonValues));
            attributeService.saveOrUpdateBatch(commonAttributeModels);
//        });
        }
        return commonEntityList;
    }

    @Override
    public Map<String, Object> getDbInfoList(String keyword, Integer pageNumber, Integer pageSize) {
        QueryWrapper<HostInfoModel> queryWrap = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrap.like("another_name", keyword).or().like("host_ip", keyword).or().like("db_name", keyword).or().like("port", keyword);
        }
        List<HostInfoModel> hostInfoModels = this.list(queryWrap);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        return new ListPageUtil<HostInfoModel>().separatePageList(hostInfoModels, requestParams);
    }

    /**
     * 上传需求清单文件
     * @param multipartFiles
     * @return
     */
    @Override
    public Object importTaskXml(List<MultipartFile> multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                // 创建SAXReader对象
                SAXReader saxReader = new SAXReader();
                // 读取XML文件，获取Document对象
                org.dom4j.Document document = saxReader.read(multipartFile.getInputStream());
                // 遍历根节点,并判断是什么类型清单
                org.dom4j.Element rootElement = document.getRootElement();
                // 查询数据库表的Entity_id
                List<String> selectId = DatabaseUtils.selectId();

                // 如果这个xml是任务清单,任务清单没有owner
                if (rootElement.getName().equals("missionInventory")) {
                    // 拿到任务清单中所有mission元素
                    org.dom4j.Element missionNode = (org.dom4j.Element) document.selectSingleNode("//mission");
                    if (missionNode != null) {
                        String missionId = missionNode.attributeValue("id");// 代表entity_id
                        String missionName = missionNode.attributeValue("name");

                        insertIntoAttAndValue(missionNode, missionId);

                        if (selectId.size() != 0) {
                            for (String id : selectId) {
                                if (!id.equals(missionId)) {
                                    // 插入实体数据
                                    DatabaseUtils.insertEntity(missionId, missionName, "mission");
                                }
                            }
                        } else {
                            // 插入实体数据
                            DatabaseUtils.insertEntity(missionId, missionName, "mission");
                        }
                        // 拿到mission下的活动
                        List<org.dom4j.Element> elements = missionNode.elements("activity");
                        for (org.dom4j.Element actElement : elements) {
                            String actId = actElement.attributeValue("id");
                            String actName = actElement.attributeValue("name");

                            insertIntoAttAndValue(actElement, actId);

                            if (selectId.size() != 0) {
                                for (String id : selectId) {
                                    if (!id.equals(actId)) {
                                        // 插入实体数据
                                        DatabaseUtils.insertEntity(actId, actName, "activity");
                                    }
                                }
                            } else {
                                DatabaseUtils.insertEntity(actId, actName, "activity");
                            }
                            // 插入关系数据
                            String relationshipId = UUID.randomUUID().toString();
                            DatabaseUtils.insertRelationship(relationshipId, "关联", missionId, actId);
                        }

                    } else {
                        throw new RuntimeException("未找到匹配节点");
                    }
                    // 找到所有的作战概念
                    List<org.dom4j.Element> metanodes = document.selectNodes("//metanode");
                    if (metanodes != null) {
                        for (org.dom4j.Element element : metanodes) {
                            String metanodeId = element.attributeValue("id");
                            String metanodeName = element.attributeValue("name");
                            DatabaseUtils.insertEntity(metanodeId, metanodeName, "metanode");

                            insertIntoAttAndValue(element, metanodeId);

                            List<org.dom4j.Element> systems = element.elements();
                            for (org.dom4j.Element system : systems) {
                                String systemName = system.attributeValue("name");
                                String systemId = system.attributeValue("id");
                                DatabaseUtils.insertEntity(systemId, systemName, "metanode");

                                insertIntoAttAndValue(system, systemId);
                                // 插入关系数据
                                String relationshipId = UUID.randomUUID().toString();
                                DatabaseUtils.insertRelationship(relationshipId, "关联", metanodeId, systemId);
                            }
                        }
                    } else {
                        throw new RuntimeException("未找到匹配节点");
                    }
                }
                // 如果是能力清单
                if (rootElement.getName().equals("capabilityInventory")) {
                    // 遍历右侧树选中的能力
                    org.dom4j.Element root = document.getRootElement();
                    String owner = root.attributeValue("owner");
                    // 根据选中的能力名称在能力清单中查到元素
                    List<org.dom4j.Element> selectNodes = document.selectNodes("//capability");
                    for (org.dom4j.Element element : selectNodes) {
                        String capId = element.attributeValue("id");
                        String capName = element.attributeValue("name");

                        if (selectId.size() != 0) {
                            for (String id : selectId) {
                                if (!id.equals(capId)) {
                                    // 插入实体数据
                                    DatabaseUtils.insertEntity(capId, capName, "capability");
                                }
                            }
                        } else {
                            DatabaseUtils.insertEntity(capId, capName, "capability");
                        }

                        insertIntoAttAndValue(element, capId);

                        // 插入关系数据
                        String relationshipId = UUID.randomUUID().toString();
                        DatabaseUtils.insertRelationship(relationshipId, "关联", owner, capId);
                    }
                }
                // 如果是装备清单
                if (rootElement.getName().equals("equipmentInventory")) {
                    org.dom4j.Element root = document.getRootElement();
                    String owner = root.attributeValue("owner");
                    // 根据选中的装备名称在装备清单中查到元素
                    List<org.dom4j.Element> selectNodes = document.selectNodes("//system");
                    for (org.dom4j.Element element : selectNodes) {
                        String systemId = element.attributeValue("id");
                        String systemName = element.attributeValue("name");

                        if (selectId.size() != 0) {
                            for (String id : selectId) {
                                if (!id.equals(systemId)) {
                                    // 插入实体数据
                                    DatabaseUtils.insertEntity(systemId, systemName, "system");
                                }
                            }
                        } else {
                            DatabaseUtils.insertEntity(systemId, systemName, "system");
                        }

                        insertIntoAttAndValue(element, systemId);

                        // 插入关系数据
                        String relationshipId = UUID.randomUUID().toString();
                        DatabaseUtils.insertRelationship(relationshipId, "关联", owner, systemId);

                        // 拿到system的所有function
                        List<org.dom4j.Element> functions = element.elements("function");
                        for (org.dom4j.Element function : functions) {
                            String functionId = function.attributeValue("id");
                            String functionName = function.attributeValue("name");

                            if (selectId.size() != 0) {
                                for (String id : selectId) {
                                    if (!id.equals(systemId)) {
                                        // 插入实体数据
                                        DatabaseUtils.insertEntity(functionId, functionName, "function");
                                    }
                                }
                            } else {
                                DatabaseUtils.insertEntity(functionId, functionName, "function");
                            }

                            insertIntoAttAndValue(function, functionId);

                            // 插入关系数据
                            String relationshipId1 = UUID.randomUUID().toString();
                            DatabaseUtils.insertRelationship(relationshipId1, "关联", systemId, functionId);
                        }

                    }

                }
                // 如果是活动清单
                if (rootElement.getName().equals("activityInventory")) {
                    // 遍历右侧树选中的能力
                    org.dom4j.Element root = document.getRootElement();
                    String owner = root.attributeValue("owner");
                    // 根据选中的能力名称在能力清单中查到元素
                    List<org.dom4j.Element> selectNodes = document.selectNodes("//activity");
                    for (org.dom4j.Element element : selectNodes) {
                        String actId = element.attributeValue("id");
                        String actName = element.attributeValue("name");

                        if (selectId.size() != 0) {
                            for (String id : selectId) {
                                if (!id.equals(actId)) {
                                    // 插入实体数据
                                    DatabaseUtils.insertEntity(actId, actName, "activity");
                                }
                            }
                        } else {
                            DatabaseUtils.insertEntity(actId, actName, "activity");
                        }

                        insertIntoAttAndValue(element, actId);

                        // 插入关系数据
                        String relationshipId = UUID.randomUUID().toString();
                        DatabaseUtils.insertRelationship(relationshipId, "关联", owner, actId);
                    }

                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 定义Lambda表达式，判断RelationshipModel对象的EntityId1或EntityId2是否与目标ID满足其一相同
     *
     * @param targetId
     * @return
     */
    public static Filter<RelationshipModel> getRelationshipByParams(String targetId) {
        return relationshipModel -> (relationshipModel.getEntityId1().equals(targetId) || relationshipModel.getEntityId2().equals(targetId));
    }

    private static Filter<ValueModel> getValueModelByParams(String targetId) {
        return valueModel -> valueModel.getEntityId().equals(targetId);
    }

    /**
     * 定义Lambda表达式，判断AttributeModel对象的id在另一个List中是否存在相同值
     *
     * @param attributeModel
     * @return
     */
    private static Filter<AttributeModel> getByParams(List<ValueModel> attributeModel) {
        return valueModel -> CollUtil.contains(attributeModel, attribute -> attribute.getAttributeId().equals(valueModel.getAttributeId()));
    }

    public void insertIntoAttAndValue(org.dom4j.Element element, String id) {
        // 拿到所有属性
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            if (!attributeName.equals("id") && !attributeName.equals("name")) {
                String attributeValue = attribute.getValue();
                String replaceName = attributeName.replace("-", " ");
                // 插入属性数据
                String attributeId = UUID.randomUUID().toString();
                DatabaseUtils.insertAttribute(attributeId, replaceName);
                // 插入value数据
                DatabaseUtils.insertValue(UUID.randomUUID().toString(), id, attributeId, attributeValue);
            }

        }
    }
}