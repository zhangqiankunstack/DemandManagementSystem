package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.OpinionModel;
import com.rengu.entity.Result;
import com.rengu.service.OpinionService;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName OpinionController
 * @Description 专家意见表控制器
 * @Author zj
 * @Date 2023/08/11 17:23
 **/
@RestController
@RequestMapping("/opinion-model")
@Api(value = "OpinionController", tags = {"专家意见表控制器"})
public class OpinionController {

    @Autowired
    public OpinionService opinionModelService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "展示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
    })
    public IPage<OpinionModel> get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        return opinionModelService.page(new Page<>(pageNum, pageSize));

    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public OpinionModel get(String Id) {
        return opinionModelService.getById(Id);
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public boolean remove(String Id) {
        return opinionModelService.removeById(Id);
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public boolean saveOrUpdate(@RequestBody OpinionModel opinionModel) {
        return opinionModelService.saveOrUpdate(opinionModel);
    }


//	@PostMapping("/saveThing")
//	@ApiOperation(value = "批量保存专家意见")
//	public Result saveThing(@RequestBody List<OpinionModel> opinionList){
//		;
//		return ResultUtils.build(opinionModelService.batchInsert(opinionList));
//	}


}
