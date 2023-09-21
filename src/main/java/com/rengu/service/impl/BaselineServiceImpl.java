package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.ToJson;
import com.rengu.mapper.*;
import com.rengu.service.BaselineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BaselineServiceImpl
 * @Description 基线服务接口实现
 * @Author zj
 * @Date 2023/08/15 16:46
 **/
@Service
public class BaselineServiceImpl extends ServiceImpl<BaselineMapper, BaselineModel> implements BaselineService {
    @Autowired
    private EntityBaselineMapper entityBaselineMapper;

    @Autowired
    private EntityHistoryMapper entityHistoryMapper;

    @Autowired
    private ValueHistoryMapper valueHistoryMapper;

    @Autowired
    private AttributeHistoryMapper attributeHistoryMapper;

    @Autowired
    private RelationshipHistoryMapper relationshipHistoryMapper;




    @Override
    public List<BaselineModel> findBaselineModelByNameAndDescription(String name, String description) {

        QueryWrapper<BaselineModel> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("baseline_name", name);
        }
        if (description != null && !description.isEmpty()) {
            queryWrapper.like("baseline_description", description);
        }
        queryWrapper.orderByAsc("priority");

        return baseMapper.selectList(queryWrapper);

    }


    @Override
    public void addBaseline(String baselineName, String baselineDescription, Integer priority) {
        BaselineModel baseline = new BaselineModel();
        baseline.setBaselineName(baselineName);
        baseline.setBaselineDescription(baselineDescription);
        baseline.setCreatedTime(new Date());
        baseline.setPriority(priority);
        baseMapper.insert(baseline);

		List<EntityHistoryModel> entityHistoryModels = entityHistoryMapper.selectList(null);
		for (EntityHistoryModel entityHistoryModel : entityHistoryModels) {
			EntityBaselineModel entityBaselineModel = new EntityBaselineModel();
			entityBaselineModel.setEntityHistoryid(entityHistoryModel.getEntityHistoryid());
			entityBaselineModel.setBaselineId(baseline.getId());
			entityBaselineMapper.insert(entityBaselineModel);
		}

	}

    @Override
    public void updateBaseline(String id, String baselineName, String baselineDescription, Integer priority) {
        BaselineModel baseline = new BaselineModel();
        baseline.setId(id);
        baseline.setBaselineName(baselineName);
        baseline.setBaselineDescription(baselineDescription);
        baseline.setPriority(priority);
        baseline.setModifiedTime(new Date());

        baseMapper.updateById(baseline);
    }



    //把数据转为json后实现上传
    @Override
    public ToJson allForDownload(Integer id){
        List<String> entityHistoryIdByBaseLineId = entityBaselineMapper.findEntityHistoryIdByBaseLineId(id);

        List<EntityHistoryModel> entityHistoryModelList = new ArrayList<>();
        List<ValueHistoryModel> valueHistoryModelList = new ArrayList<>();
        List<AttributeHistoryModel> attributeHistoryModelList = new ArrayList<>();
        List<RelationshipHistoryModel> relationshipHistoryModelList = new ArrayList<>();

        for (String entityHistoryId : entityHistoryIdByBaseLineId) {
            EntityHistoryModel entityHistoryByEntityHistoryId = entityHistoryMapper.getEntityHistoryByEntityHistoryId(entityHistoryId);
            entityHistoryModelList.add(entityHistoryByEntityHistoryId);

            List<ValueHistoryModel> valueHistoryModelByEntityId = valueHistoryMapper.findValueHistoryModelByEntityId(entityHistoryByEntityHistoryId.getEntityId());
            for (ValueHistoryModel valueHistoryModel : valueHistoryModelByEntityId) {
                valueHistoryModelList.add(valueHistoryModel);

                AttributeHistoryModel byAttributeId = attributeHistoryMapper.findByAttributeId(valueHistoryModel.getAttributeId());
                attributeHistoryModelList.add(byAttributeId);
            }


            List<RelationshipHistoryModel> byEntityHistoryId = relationshipHistoryMapper.findByEntityHistoryId(entityHistoryByEntityHistoryId.getEntityId());
            for (RelationshipHistoryModel relationshipHistoryModel : byEntityHistoryId) {
                relationshipHistoryModelList.add(relationshipHistoryModel);
            }



        }
        ToJson toJson =new ToJson();
        toJson.setEntityHistorys(entityHistoryModelList);
        toJson.setValueHistory(valueHistoryModelList);
        toJson.setAttributeHistory(attributeHistoryModelList);
        toJson.setLinks(relationshipHistoryModelList);

        return toJson;





//
//        return toJson;
//
//
//
//        //todo:初始化导出目录
//        File exportPath = new File(FileUtils.getTempDirectoryPath() + File.separator + "基线");
//        exportPath.mkdirs();
//        //导出成果文件
//        for (ToolPackageFileEntity toolPackageFileEntity : getToolPackageFileByParentNodeAndToolPackage(null, toolPackageArgs)) {
//            exportToolPackageFile(toolPackageFileEntity, exportPath);
//        }
//        return exportPath;
    }

}