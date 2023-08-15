package com.rengu.controller;

import com.rengu.entity.*;
import com.rengu.service.HostInfoService;
import com.rengu.util.DataBaseFactoryService;
import com.rengu.util.RedisKeyPrefix;
import com.rengu.util.RedisUtils;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
    @Autowired
    private RedisUtils redisUtils;

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

    @ApiOperation("分页模糊查询数据配置")
    @GetMapping(value = "/getDbInfoList")
    public Result getDbInfoList(@RequestParam(required = false) String keyWord, @RequestParam() Integer pageNumber, @RequestParam() Integer pageSize) {
        return ResultUtils.build(hostInfoService.getDbInfoList(keyWord, pageNumber, pageSize));
    }

    /**
     * 保存元数据实体、关联关系、属性
     *
     * @param entityIds
     * @return
     */
    @ApiOperation(value = "保存元数据实体、关联关系、属性")
    @PostMapping("saveMetadata")
    public Result saveMetadata(@RequestBody List<String> entityIds) {
        List<EntityModel> entityModels = (List<EntityModel>) redisUtils.get(RedisKeyPrefix.ENTITY);
        List<RelationshipModel> relationshipModels = (List<RelationshipModel>) redisUtils.get(RedisKeyPrefix.RELATIONSHIP);
        List<AttributeModel> attributeModels = (List<AttributeModel>) redisUtils.get(RedisKeyPrefix.ATTRIBUTE);
        List<ValueModel> valueModels = (List<ValueModel>) redisUtils.get(RedisKeyPrefix.VALUE);
        return ResultUtils.build(hostInfoService.saveMetadata(entityModels, relationshipModels, attributeModels, valueModels, entityIds));
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
