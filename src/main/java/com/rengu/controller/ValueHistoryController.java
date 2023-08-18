package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.Result;
import com.rengu.entity.ValueHistoryModel;
import com.rengu.service.ValueHistoryService;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ValueHistoryController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@RestController
@RequestMapping("/value-history-model")
@Api(value = "ValueHistoryController", tags = {"历史value控制器"})
public class ValueHistoryController {

    @Autowired
    public ValueHistoryService valueHistoryModelService;

    @ApiOperation("查询数据值列表（本地数据）")
    @GetMapping("/getAllValueInfo")
    public Result getAllValueInfo(@RequestParam String entityId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(valueHistoryModelService.getAllValueInfo(entityId, keyWord, pageNumber, pageSize));
    }
}
