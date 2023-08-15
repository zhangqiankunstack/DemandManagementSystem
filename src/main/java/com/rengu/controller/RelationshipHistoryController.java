package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.service.RelationshipHistoryService;
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
@Api(value = "RelationshipHistoryController", tags = {"控制器"})
public class RelationshipHistoryController {

    @Autowired
    public RelationshipHistoryService relationshipHistoryModelService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "展示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
    })
    public IPage<RelationshipHistoryModel> get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        return relationshipHistoryModelService.page(new Page<>(pageNum, pageSize));

    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public RelationshipHistoryModel get(String Id) {
        return relationshipHistoryModelService.getById(Id);
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public boolean remove(String Id) {
        return relationshipHistoryModelService.removeById(Id);
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public boolean saveOrUpdate(@RequestBody RelationshipHistoryModel relationshipHistoryModel) {
        return relationshipHistoryModelService.saveOrUpdate(relationshipHistoryModel);
    }
}
