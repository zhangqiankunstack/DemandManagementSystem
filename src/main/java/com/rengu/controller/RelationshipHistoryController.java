package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.entity.Result;
import com.rengu.service.RelationshipHistoryService;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName RelationshipHistoryController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@RestController
@RequestMapping("/relationship-history-model")
@Api(value = "RelationshipHistoryController", tags = {"历史关系控制器"})
public class RelationshipHistoryController {

    @Autowired
    public RelationshipHistoryService relationshipHistoryModelService;


    @ApiOperation("查询历史关联关系（本地数据）")
    @GetMapping("/getAllRelationship")
    public Result getAllRelationship(@RequestParam String entityId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(relationshipHistoryModelService.getAllRelationship(entityId, keyWord, pageNumber, pageSize));
    }

}
