package com.rengu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.entity.Result;
import com.rengu.entity.vo.*;
import com.rengu.mapper.RelationshipHistoryMapper;
import com.rengu.service.EntityHistoryService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName EntityHistoryController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@RestController
@RequestMapping("/entity-history-model")
@Api(value = "EntityHistoryController", tags = {"历史实体表控制器"})
public class EntityHistoryController {

    @Autowired
    public EntityHistoryService entityHistoryModelService;

    @Autowired
    private RelationshipHistoryMapper relationshipHistoryMapper;

    @ApiOperation("分页模糊查询历史实体列表（本地数据）isTop为1及置顶")
    @GetMapping("/getAllEntityHistory")
    public Result getAllEntity(@RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(entityHistoryModelService.getAllEntity(keyWord, pageNumber, pageSize));
    }


    @ApiOperation("分页模糊查询所有存在的历史实体")
    @GetMapping("/getAllNowEntityHistory")
    public Result getAllNowEntityHistory(@RequestParam(required = false) String entityId,@RequestParam(required = false)String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(entityHistoryModelService.getAllNowEntityHistory(entityId,keyWord, pageNumber, pageSize));
    }

    @ApiOperation("查询单个存在的历史实体")
    @GetMapping("/getThisEH")
    public Result getThisEH(String id) {

        return ResultUtils.build(entityHistoryModelService.getById(id));
    }

    @ApiOperation(value = "恢复")
    @PostMapping("/recover")
    public Result recover(String id) {
        entityHistoryModelService.recover(id);
        return ResultUtils.build("恢复成功");
    }


    @ApiOperation("版本比对")
    @GetMapping("/findValueByEntityHistoryId")
    public Result findValueByEntityHistoryId(String id1) {
        List<ValueAttributeEntityVo> valueByEntityHistoryId1 = entityHistoryModelService.findValueByEntityHistoryId(id1);
//        List<ValueAttributeEntityVo> valueByEntityHistoryId2 = entityHistoryModelService.findValueByEntityHistoryId(id2);
//        TwoValueAttributeEntityVo two = new TwoValueAttributeEntityVo();
//        two.setValueList1(valueByEntityHistoryId1);
//        two.setValueList2(valueByEntityHistoryId2);
        return ResultUtils.build(valueByEntityHistoryId1);
    }








    @ApiOperation("查询历史关联关系")
    @GetMapping("/getAllRelationship")
    public Result getAllRelationship(@RequestParam String entityHistoryId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
//        List<EntityHistoryRelationship> relatedEntities = entityHistoryModelService.getRelatedEntities(entityHistoryId, entityType);
        List<EntityHistoryRelationship> relatedEntities = new ArrayList<>();

        //获取到所有对的etityHistory
        Map<String, EntityHistoryModel> entityHistoryMap = entityHistoryModelService.list().stream().collect(Collectors.toMap(EntityHistoryModel::getEntityHistoryid, Function.identity()));
        relationshipHistoryMapper.selectList(new LambdaQueryWrapper<RelationshipHistoryModel>().eq(RelationshipHistoryModel::getEntityHistoryId1, entityHistoryId)
                .or(wrapper -> wrapper.eq(RelationshipHistoryModel::getEntityHistoryId2, entityHistoryId))).stream().forEach(r ->{
                    EntityHistoryModel entity1 = entityHistoryMap.get(r.getEntityHistoryId1());
                    EntityHistoryModel entity2 = entityHistoryMap.get(r.getEntityHistoryId2());
                    EntityHistoryRelationship result = new EntityHistoryRelationship();
                    if(entityHistoryId.equals(r.getEntityHistoryId1()) && entity2 != null && (StringUtils.isEmpty(keyWord) || entity2.getEntityType().equals(keyWord))){
                        result.setEntityId1(entity2.getEntityHistoryid());
                        result.setEntityName1(entity2.getEntityName());
                        result.setEntityType(entity2.getEntityType());
                        result.setRelationshipType(r.getRelationshipType());
                        relatedEntities.add(result);
                    }else if(entityHistoryId.equals(r.getEntityHistoryId2()) && entity1 != null && (StringUtils.isEmpty(keyWord) || entity2.getEntityType().equals(keyWord))){
                        result.setEntityId1(entity1.getEntityHistoryid());
                        result.setEntityName1(entity1.getEntityName());
                        result.setEntityType(entity1.getEntityType());
                        result.setRelationshipType(r.getRelationshipType());
                        relatedEntities.add(result);
                    }
                });


        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityHistoryRelationship>().separatePageList(relatedEntities, requestParams);
        return ResultUtils.build(requestParams);

    }





    @ApiOperation("查询该实体以外的历史版本")
    @GetMapping("/getOther")
    public Result getOther(String entityId ,String entityHistoryId ) {

        return ResultUtils.build(entityHistoryModelService.getEntityHistoryByEntityId(entityId, entityHistoryId));

    }


}

