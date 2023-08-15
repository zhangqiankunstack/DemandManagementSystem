package com.rengu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.HostInfoMapper;
import com.rengu.mapper.ValueAttributeMapper;
import com.rengu.service.*;
import com.rengu.util.ListPageUtil;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RedisTemplateUtil redisTemplateUtil;
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
//                entityMapper.insert(entityModel);
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

    // 定义Lambda表达式，判断RelationshipModel对象的EntityId1或EntityId2是否与目标ID满足其一相同
    public static Filter<RelationshipModel> getRelationshipByParams(String targetId) {
        return relationshipModel -> relationshipModel.getEntityId1().equals(targetId) || relationshipModel.getEntityId2().equals(targetId);
    }

    private static Filter<ValueModel> getValueModelByParams(String targetId) {
        return valueModel -> valueModel.getEntityId().equals(targetId);
    }

    // 定义Lambda表达式，判断AttributeModel对象的id在另一个List中是否存在相同值
    private static Filter<AttributeModel> getByParams(List<ValueModel> attributeModel) {
        return valueModel -> CollUtil.contains(attributeModel, attribute -> attribute.getAttributeId().equals(valueModel.getAttributeId()));
    }
}