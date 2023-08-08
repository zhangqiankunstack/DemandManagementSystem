package com.rengu.controller;

import com.rengu.entity.HostInfoModel;
import com.rengu.entity.Result;
import com.rengu.service.HostInfoService;
import com.rengu.util.DataBaseFactoryService;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @program
 * @Author Miracle ZHQK
 * @Description:数据配置管理
 * @Date 2023/08/07 15:22
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/dataConfiguration")
@Api(tags = "数据配置")
public class DataConfigurationController {

    @Autowired
    private HostInfoService hostInfoService;

    @ApiOperation("测试连接数据库")
    @PostMapping(value = "/testConnect")
    public Result testConnect(@RequestBody HostInfoModel dbInfo) {
        return ResultUtils.build(DataBaseFactoryService.getDbFactory().testConnect(dbInfo) ? "连接成功" : "连接失败");
    }

    @ApiOperation("保存数据库信息/修改数据库信息")
    @PostMapping(value = "/saveOrUpdateDbInfo")
    public Result saveOrUpdateDbInfo(@RequestBody HostInfoModel dbInfo) {
        return ResultUtils.build(hostInfoService.saveOrUpdateDbInfo(dbInfo));
    }

    @ApiOperation("根据数据库信息id删除信息")
    @DeleteMapping(value = "/{dbInfoId}/deletedDbInfoById")
    public Result deletedDbInfoById(@PathVariable(value = "dbInfoId") String dbInfoId) {
        return ResultUtils.build(hostInfoService.deletedDbInfoById(dbInfoId));
    }

    @ApiOperation(value = "通过数据库id查询出数据库信息")
    @GetMapping(value = "/{dbInfoId}/getDbInfo")
    public Result getDbInfo(@PathVariable(value = "dbInfoId") String dbInfoId) {
        return ResultUtils.build(hostInfoService.getDbInfoById(dbInfoId));
    }

    /**
     * 上传任务清单并解析(测试接口)
     *
     * @param multipartFiles
     * @return
     */
    @ApiOperation(value = "上传任务清单")
    @PostMapping(value = "importTaskFiles")
    public Result importTaskFiles(@ApiParam(value = "multipartFile") @RequestParam MultipartFile multipartFiles) {
        return ResultUtils.build(hostInfoService.importTaskFiles(multipartFiles));
    }
}
