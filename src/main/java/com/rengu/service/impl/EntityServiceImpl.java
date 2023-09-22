package com.rengu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.*;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.RelationshipMapper;
import com.rengu.mapper.TemplateMapper;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.*;
import com.rengu.util.ExportMyWord;
import com.rengu.util.FtlUtils;
import com.rengu.util.ListPageUtil;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName EntityServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Service
public class EntityServiceImpl extends ServiceImpl<EntityMapper, EntityModel> implements EntityService {

    private static final Parser parser = Parser.builder().build();

    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private static final String FUNCTION = "function";
    private static final String SYSTEM = "system";
    private static final String CAPABILITY = "capability";
    private static final String ACTIVITY = "activity";
    @Value("${mysql.entitySql}")
    private String entitySql;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private RelationshipMapper relationshipMapper;

    @Autowired
    private HostInfoServiceImpl hostInfoServiceImpl;

    @Autowired
    private ValueService valueService;

    @Autowired
    private EntityHistoryService entityHistoryService;

    @Autowired
    private RequirementService requirementService;

    /**
     * 连接后查询
     *
     * @param hostInfo
     * @param keyWord
     * @return
     */
    @Override
    public List<EntityModel> connect(HostInfoModel hostInfo, String keyWord) {
        String databaseUrl = "jdbc:kingbase8://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
//        String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getDbName() + "?serverTimezone=GMT";
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
    public Map<String, Object> findTrace(String type) {
        Map<String, Object> map = null;
        if (type.equals("1")) {
            map = missionAndCapabilityTrace();//任务与能力
        }
        if (type.equals("2")) {
            map = CapabilityAndSystemTrace();//能力与系统
        }
        return map;
    }

    /**
     * 覆盖分析追溯矩阵
     *
     * @return
     */
    @Override
    public List<Object> coverageAnalysisTrace() {
        List<TraceVo> sysTraceVos = new ArrayList<>();

        QueryWrapper<EntityModel> query = new QueryWrapper<>();
        query.eq("entity_type", SYSTEM);
        List<EntityModel> systems = this.list(query);//列系统

        systems.stream().forEach(system -> {
            List<RelationshipModel> modelList = relationshipService.list();
            List<RelationshipModel> commonRelationList = CollUtil.filter(modelList, hostInfoServiceImpl.getRelationshipByParams(system.getEntityId()));
            List<EntityModel> functionEntities = new ArrayList<>();
            commonRelationList.stream().forEach(relationshipModel -> {
                EntityModel entityModel1 = this.getById(relationshipModel.getEntityId1());

                if (relationshipModel.getEntityId1() != system.getEntityId() && entityModel1 != null && entityModel1.getEntityType().equals(FUNCTION)) {
                    functionEntities.add(this.getById(relationshipModel.getEntityId1()));
                }
                EntityModel entityModel2 = this.getById(relationshipModel.getEntityId2());
                if (relationshipModel.getEntityId2() != system.getEntityId() && entityModel2 != null && entityModel2.getEntityType().equals(FUNCTION) && !functionEntities.contains(entityModel2)) {
                    functionEntities.add(this.getById(relationshipModel.getEntityId2()));
                }
            });
            if (functionEntities.size() > 0) {
                TraceVo traceVo = new TraceVo();
//                BeanUtils.copyProperties(system, traceVo, "entities");
                traceVo.setEntityName(system.getEntityName());
                traceVo.setEntities(functionEntities);
                sysTraceVos.add(traceVo);
            }
        });

        query.clear();

        query.eq("entity_type", CAPABILITY);
        List<EntityModel> capabilities = this.list(query);//行能力
        List<TraceVo> capTraceVos = new ArrayList<>();
        capabilities.stream().forEach(capability -> {
            List<RelationshipModel> modelList1 = relationshipService.list();
            List<RelationshipModel> commonRelationList = CollUtil.filter(modelList1, hostInfoServiceImpl.getRelationshipByParams(capability.getEntityId()));
            List<EntityModel> activityEntities = new ArrayList<>();
            commonRelationList.stream().forEach(relationshipModel -> {
                EntityModel entityModel1 = this.getById(relationshipModel.getEntityId1());
                if (relationshipModel.getEntityId1() != capability.getEntityId() && entityModel1 != null && entityModel1.getEntityType().equals(ACTIVITY)) {
                    activityEntities.add(this.getById(relationshipModel.getEntityId1()));
                }
                EntityModel entityModel2 = this.getById(relationshipModel.getEntityId2());
                if (relationshipModel.getEntityId2() != capability.getEntityId() && entityModel2 != null && entityModel2.getEntityType().equals(ACTIVITY) && !activityEntities.contains(entityModel2)) {
                    activityEntities.add(this.getById(relationshipModel.getEntityId2()));
                }
            });
            if (activityEntities.size() > 0) {
                TraceVo traceVo = new TraceVo();
                traceVo.setEntityName(capability.getEntityName());
//                BeanUtils.copyProperties(capability, traceVo, "entities");
                traceVo.setEntities(activityEntities);
                capTraceVos.add(traceVo);
            }
        });

        List<Object> objList = new ArrayList<>();
        //列
        sysTraceVos.stream().forEach(sysTraceVo -> {
            List<Map> mapList = new ArrayList<>();
            //行
            capTraceVos.stream().forEach(capTraceVo -> {
                List<List<Boolean>> listList = new ArrayList<>();
                //todo:列

                sysTraceVo.getEntities().stream().forEach(function -> {
                    List<Boolean> boolList = new ArrayList<>();
                    //todo:行
                    capTraceVo.getEntities().stream().forEach(cap -> {

                        List<RelationshipModel> modelList = relationshipService.list();
                        List<RelationshipModel> commonRelationList = CollUtil.filter(modelList, hostInfoServiceImpl.getRelationshipByParams(function.getEntityId()));
                        boolean bool = false;
                        for (RelationshipModel relationshipModel : commonRelationList) {
                            if (relationshipModel.getEntityId1().equals(cap.getEntityId()) || relationshipModel.getEntityId2().equals(cap.getEntityId())) {
                                bool = true;
                                System.out.println("进入true,并跳出循环");
                                break;
                            }
                        }
                        System.out.println("执行外面的语句：" + bool);
                        boolList.add(bool);
                    });
                    listList.add(boolList);
                });
                Map<String, Object> map = new HashMap<>();
                map.put("system", sysTraceVo);
                map.put("capabilities", capTraceVo);
                map.put("boolList", listList);
                mapList.add(map);
            });
            objList.add(mapList);
        });
        return objList;
    }

    @Override
    public boolean deletedById(String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        //删除关联关系
        List<RelationshipModel> modelList = relationshipService.list();
        List<RelationshipModel> relationList = CollUtil.filter(modelList, hostInfoServiceImpl.getRelationshipByParams(id));
        relationList.forEach(relationshipModel -> {
            relationshipService.removeById(relationshipModel.getRelationshipId());
        });
//        relationshipService.removeByIds(relationList);
        //删除value、attribute
        valueService.deleteByEntityId(id);
        //删除历史
        entityHistoryService.deleteByEntityId(id);
        //删除需求描述
        requirementService.deleteByEntityId(id);
        return this.removeById(id);
    }


    // //列
    //        sysTraceVos.stream().forEach(sysTraceVo -> {
    //            List<Map> mapList = new ArrayList<>();
    //            //行
    //            capTraceVos.stream().forEach(capTraceVo -> {
    //                List<List<Boolean>> listList = new ArrayList<>();
    //                //todo:列
    //                sysTraceVo.getEntities().stream().forEach(function -> {
    //                    List<Boolean> boolList = new ArrayList<>();
    //                    //todo:行
    //                    capTraceVo.getEntities().stream().forEach(cap -> {
    //
    //                        boolean bool = relationshipService.getRelationshipByEntityIds(function.getEntityId(), cap.getEntityId());
    //                        boolList.add(bool);
    //                    });
    //                    listList.add(boolList);//
    //                });
    //                Map<String, Object> map = new HashMap<>();
    //                map.put("system", sysTraceVo);
    //                map.put("capabilities", capTraceVo);
    //                map.put("boolList", listList);
    //                mapList.add(map);
    //            });
    //            objList.add(mapList);
    //        });

    /**
     * 能力与系统需求追溯矩阵
     *
     * @return
     */
    public Map<String, Object> CapabilityAndSystemTrace() {
        QueryWrapper<EntityModel> queryWrap = new QueryWrapper<>();
        queryWrap.eq("entity_type", "capability");
        List<EntityModel> capabilities = this.list(queryWrap);//行(能力)
        queryWrap.clear();
        queryWrap.eq("entity_type", SYSTEM);
        List<EntityModel> systems = this.list(queryWrap);//列（系统）
        List<List<Boolean>> listList = new ArrayList<>();
        systems.stream().forEach(system -> {
            List<Boolean> boolList = new ArrayList<>();
            capabilities.stream().forEach(capability -> {
                boolean bool = relationshipService.getRelationshipByEntityIds(system.getEntityId(), capability.getEntityId());
                boolList.add(bool);
            });
            listList.add(boolList);
        });
        Map<String, Object> map = new HashMap<>();
//        map.put("systems", systems);
        map.put("missions", systems);
        map.put("capabilities", capabilities);
        map.put("boolList", listList);
        return map;
    }

    /**
     * 任务与能力需求追溯矩阵
     *
     * @return
     */
    public Map<String, Object> missionAndCapabilityTrace() {
        QueryWrapper<EntityModel> queryWrap = new QueryWrapper<>();
        queryWrap.eq("entity_type", "mission");
        List<EntityModel> missions = this.list(queryWrap);//行
        queryWrap.clear();
        queryWrap.eq("entity_type", "capability");
        List<EntityModel> capabilities = this.list(queryWrap);//列
        List<List<Boolean>> listList = new ArrayList<>();
        capabilities.stream().forEach(capability -> {
            List<Boolean> boolList = new ArrayList<>();
            missions.stream().forEach(mission -> {
                boolean bool = relationshipService.getRelationshipByEntityIds(capability.getEntityId(), mission.getEntityId());
                boolList.add(bool);
            });
            listList.add(boolList);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("missions", missions);
        map.put("capabilities", capabilities);
        map.put("boolList", listList);
        return map;
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


    public static void main(String[] args) {
        String configFile = "classpath:application.yml";

        // 读取YAML配置文件
        Yaml yaml = new Yaml();
        try {
            // 创建ResourceLoader对象
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            // 获取配置文件的资源对象
            Resource resource = resourceLoader.getResource(configFile);
            // 获取配置文件的路径
            String filePath = resource.getFile().getAbsolutePath();
            System.out.println("配置文件路径：" + filePath);
            FileInputStream inputStream = new FileInputStream(filePath);
            Map<String, Object> config = yaml.load(inputStream);

            // 获取jdbc URL
            String jdbcUrl = (String) config.get("url");
            String userName = (String) config.get("username");
            // 从jdbc URL中提取数据库名
            String dbName = extractDatabaseName(jdbcUrl);

            System.out.println("截取到的数据库名为：" + dbName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractDatabaseName(String jdbcUrl) {
        int endIndex = jdbcUrl.lastIndexOf("/");
        return jdbcUrl.substring(endIndex + 1);
    }

    /**
     * 导出报告
     *
     * @param base64List
     * @param templateId
     * @param response
     * @return
     */
    @Override
    public void exportReport(List<String> base64List, String templateId, HttpServletResponse response) {
        //获取模板以及相关文件信息
        TemplateWithFileVo templateWithFileVo = templateMapper.getTemplateAndFileInformationByTemplateId(templateId);
        if(templateWithFileVo == null){
            throw new IllegalArgumentException("所传templateId找不到对应模板");
        }
        if(StringUtils.isEmpty(templateWithFileVo.getLocalPath())){
            throw new IllegalArgumentException("找不到模板对应存储路径");
        }

        //获取所有任务实体列表
        LambdaQueryWrapper<EntityModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(EntityModel::getEntityType, "mission");
//        List<EntityModel> missionList = this.list(lambda);
        List<EntityExportVo> missionList = entityMapper.getEntityWithDescriptionByType("mission");
        missionList.stream().filter(e -> !StringUtils.isEmpty(e.getDescription()))
                //解析前先替换掉回车换行，取到文本后再替换回来
                .forEach(e -> e.setDescription(org.jsoup.Jsoup.parse(renderer.render(parser.parse(e.getDescription().replaceAll("\n", "#replacementForChangeLine#")))).text().replaceAll("#replacementForChangeLine#", "<w:br/>")));


        lambda.clear();
        //获取所有能力的实体列表
        lambda.eq(EntityModel::getEntityType, "capability");
//        List<EntityModel> capabilityList = this.list(lambda);
        List<EntityExportVo> capabilityList = entityMapper.getEntityWithDescriptionByType("capability");
        capabilityList.stream().filter(e -> !StringUtils.isEmpty(e.getDescription())).forEach(e -> e.setDescription(org.jsoup.Jsoup.parse(renderer.render(parser.parse(e.getDescription().replaceAll("\n", "#replacementForChangeLine#")))).text().replaceAll("#replacementForChangeLine#", "<w:br/>")));

        lambda.clear();

        //获取所有系统的实体列表
        lambda.eq(EntityModel::getEntityType, "system");
//        List<EntityModel> systemList = this.list(lambda);
        List<EntityExportVo> systemList = entityMapper.getEntityWithDescriptionByType("system");
        systemList.stream().filter(e -> !StringUtils.isEmpty(e.getDescription())).forEach(e -> e.setDescription(org.jsoup.Jsoup.parse(renderer.render(parser.parse(e.getDescription().replaceAll("\n", "#replacementForChangeLine#")))).text().replaceAll("#replacementForChangeLine#", "<w:br/>")));

        lambda.clear();

        //获取所有系统功能的实体列表
        lambda.eq(EntityModel::getEntityType, "function");
//        List<EntityModel> functionList = this.list(lambda);
        List<EntityExportVo> functionList = entityMapper.getEntityWithDescriptionByType("function");
        functionList.stream().filter(e -> !StringUtils.isEmpty(e.getDescription()))
                .forEach(e -> e.setDescription(org.jsoup.Jsoup.parse(renderer.render(parser.parse(e.getDescription().replaceAll("\n", "#replacementForChangeLine#")))).text().replaceAll("#replacementForChangeLine#", "<w:br/>")));
        //获取矩阵任务需求-能力需求矩阵
        Map<String, Object> missionMap = missionAndCapabilityTrace();
        Map<String, Object> systemMap = CapabilityAndSystemTrace();//能力与系统

        //todo：封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("missionList", missionList);
        map.put("capabilityList", capabilityList);
        map.put("systemList", systemList);
        map.put("functionList", functionList);
        map.put("missionMap", missionMap);  //任务需求-能力需求
        map.put("systemMap", systemMap);     //能力与系统
        map.put("image1", base64List.get(0));
        map.put("image2", base64List.get(1));
        map.put("image3", base64List.get(2));
//        FtlUtils.reportPeisOrgReservation(map, response);
        try {
            FtlUtils.reportPeisOrgReservation(map, templateWithFileVo.getLocalPath(), "logs/" + templateWithFileVo.getFileName() + ".docx", templateWithFileVo.getFileName() + ".docx", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportSchemeAppraisal(String templateId, String filePath, String fileName, HttpServletResponse response) {
        ExportMyWord exportMyWord = new ExportMyWord();
        HashMap<String, Object> mapData = new HashMap<>();
        //获取所有任务实体列表
        LambdaQueryWrapper<EntityModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(EntityModel::getEntityType, "mission");
        List<EntityModel> missionList = this.list(lambda);

        lambda.clear();
        //获取所有能力的实体列表
        lambda.eq(EntityModel::getEntityType, "capability");
        List<EntityModel> capabilityList = this.list(lambda);

        lambda.clear();

        //获取所有系统的实体列表
        lambda.eq(EntityModel::getEntityType, "system");
        List<EntityModel> systemList = this.list(lambda);

        lambda.clear();

        //获取所有系统功能的实体列表
        lambda.eq(EntityModel::getEntityType, "function");
        List<EntityModel> functionList = this.list(lambda);

        //获取矩阵任务需求-能力需求矩阵
        Map<String, Object> missionMap = missionAndCapabilityTrace();
        Map<String, Object> systemMap = CapabilityAndSystemTrace();//能力与系统

        //todo：封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("missionList", missionList);
        map.put("capabilityList", capabilityList);
        map.put("systemList", systemList);
        map.put("functionList", functionList);
        map.put("missionMap", missionMap);  //任务需求-能力需求
        map.put("systemMap", systemMap);     //能力与系统
//        map.put("image1", base64List.get(0));
//        map.put("image2", base64List.get(1));
//        map.put("image3", base64List.get(2));

//        exportMyWord.createWord(mapData, "方案评审证书.ftl", filePath+fileName+".doc");
        exportMyWord.createWord(mapData, "ftl导出模板.ftl", filePath+fileName+".doc");

    }

    @Override
    public List<EntityModel> fetchUnrelatedEntities(List<String> ids) {
        Set<String> relatedEntitiesIds = new HashSet<>();
        Map<String, EntityModel> entityMap = this.list().stream().collect(Collectors.toMap(EntityModel::getEntityId, Function.identity()));
        //获取到所有存在关联关系的实体id
        relationshipMapper.getRelationshipsByEntityIds(ids)
                .stream().forEach(r -> {
                   if(ids.contains(r.getEntityId1()) && entityMap.get(r.getEntityId2()) != null){
                       relatedEntitiesIds.add(r.getEntityId1());
                   }
                   if(ids.contains(r.getEntityId2()) && entityMap.get(r.getEntityId1()) != null){
                       relatedEntitiesIds.add(r.getEntityId2());
                   }
                });
        //去除掉存在关联关系的实体id
        ids.removeAll(relatedEntitiesIds);
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>();
        }
        return entityMapper.selectBatchIds(ids);
    }

}
