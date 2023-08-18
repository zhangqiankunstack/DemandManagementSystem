package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.Result;
import com.rengu.entity.vo.*;
import com.rengu.service.EntityHistoryService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiOperation("分页模糊查询历史实体列表（本地数据）isTop为1及置顶")
    @GetMapping("/getAllEntityHistory")
    public Result getAllEntity(@RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(entityHistoryModelService.getAllEntity(keyWord, pageNumber, pageSize));
    }


    @ApiOperation("分页模糊查询所有存在的历史实体")
    @GetMapping("/getAllNowEntityHistory")
    public Result getAllNowEntityHistory(@RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(entityHistoryModelService.getAllNowEntityHistory(keyWord, pageNumber, pageSize));
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
    public Result findValueByEntityHistoryId(String id1,String id2) {
        List<ValueAttributeEntityVo> valueByEntityHistoryId1 = entityHistoryModelService.findValueByEntityHistoryId(id1);
        List<ValueAttributeEntityVo> valueByEntityHistoryId2 = entityHistoryModelService.findValueByEntityHistoryId(id2);
        TwoValueAttributeEntityVo two = new TwoValueAttributeEntityVo();
        two.setValueList1(valueByEntityHistoryId1);
        two.setValueList2(valueByEntityHistoryId2);
        return ResultUtils.build(two);
    }








    @ApiOperation("查询历史关联关系")
    @GetMapping("/getAllRelationship")
    public Result getAllRelationship(@RequestParam String entityHistoryId, @RequestParam(required = false) String entityType, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<EntityHistoryRelationship> relatedEntities = entityHistoryModelService.getRelatedEntities(entityHistoryId, entityType);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityHistoryRelationship>().separatePageList(relatedEntities, requestParams);
        return ResultUtils.build(requestParams);

    }





}

