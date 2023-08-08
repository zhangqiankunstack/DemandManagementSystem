package com.rengu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.rengu.entity.*;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.Result;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.service.HostInfoService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.RedisKeyPrefix;
import com.rengu.util.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HostInfoController
 * @Description 数据库表控制器
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@RestController
@RequestMapping("/host-info-model")
@Api(value = "HostInfoController", tags = {"数据库表控制器"})
public class HostInfoController {

    @Autowired
    public HostInfoService hostInfoModelService;
    @Autowired
    private RedisUtils redisUtils;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "展示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
    })
    public Result get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {

        return Result.success(hostInfoModelService.page(new Page<>(pageNum, pageSize)));

    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public Result get(String Id) {
        return Result.success(hostInfoModelService.getById(Id));
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public Result remove(String Id) {
        return Result.success(hostInfoModelService.removeById(Id));
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public Result saveOrUpdate(@RequestBody HostInfoModel hostInfoModel) {
        return Result.success(hostInfoModelService.saveOrUpdate(hostInfoModel));
    }


    @ApiOperation(value = "测试连接", notes = "测试")
    @PostMapping("/dataTest")
    public Result dataTest(@RequestBody HostInfoModel hostInfo) {
        if (hostInfo.getStatus() == 1) {
            hostInfoModelService.databaseTest(hostInfo);
        }
        if (hostInfo.getStatus() == 2) {
        }
        return Result.success(hostInfoModelService.databaseTest(hostInfo));
    }

    /**
     * 采集任务展示实体、属性、关系（未入库数据接口）
     *
     * @param hostInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询元数据实体列表")
    @PostMapping("/test-online")
    public Result testOnline(@RequestBody HostInfoModel hostInfo, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        List<EntityModel> entityModels = hostInfoModelService.connect(hostInfo);
        if (entityModels.size() != 0) {
            redisUtils.set(RedisKeyPrefix.ENTITY, entityModels, 7200L);
        }
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNum);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityModel>().separatePageList(entityModels, requestParams);
        return Result.success(requestParams);
    }

    //保存元数据实体、关联关系、属性
    @ApiOperation(value = "保存元数据实体、关联关系、属性")
    @PostMapping("saveMetadata")
    public Result saveMetadata(@RequestBody List<String> entityIds) {
        List<EntityModel> entityModels = (List<EntityModel>) redisUtils.get(RedisKeyPrefix.ENTITY);
        List<RelationshipModel> relationshipModels = (List<RelationshipModel>) redisUtils.get(RedisKeyPrefix.RELATIONSHIP);
        List<AttributeModel> attributeModels = (List<AttributeModel>) redisUtils.get(RedisKeyPrefix.ATTRIBUTE);
        List<ValueModel> valueModels = (List<ValueModel>) redisUtils.get(RedisKeyPrefix.VALUE);
        return Result.success(hostInfoModelService.saveMetadata(entityModels, relationshipModels, attributeModels, valueModels, entityIds));
    }


    @ApiOperation(value = "查实体属性", notes = "查实体属性")
    @PostMapping("/findById/{entityId}")
    public Result findValue(@PathVariable String entityId, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        List<ValueAttribute> list = hostInfoModelService.findValueAttributesByEntityId(entityId);
        Page page = new Page();
        int size = list.size();
        if (pageSize > size) {
            pageSize = size;
        }
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (pageNum > maxPage) {
            pageNum = maxPage;
        }
        // 当前页第一条数据的下标
        int curIdx = pageNum > 1 ? (pageNum - 1) * pageSize : 0;
        List pageList = new ArrayList();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }
        page.setCurrent(pageNum).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return Result.success(page);
    }

    /**
     * 查关联属性
     *
     * @param entityId1
     * @return
     */
    @ApiOperation(value = "查关联实体", notes = "查关联实体")
    @PostMapping("/entity/{entityId1}")
    public Result getEntityId2ByEntityName(@PathVariable String entityId1, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {

        List<EntityRelationship> list = hostInfoModelService.getEntityRelationships(entityId1);
        Page page = new Page();
        int size = list.size();

        if (pageSize > size) {
            pageSize = size;
        }

        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

        if (pageNum > maxPage) {
            pageNum = maxPage;
        }

        // 当前页第一条数据的下标
        int curIdx = pageNum > 1 ? (pageNum - 1) * pageSize : 0;

        List pageList = new ArrayList();

        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(pageNum).setSize(pageSize).setTotal(list.size()).setRecords(pageList);


        return Result.success(page);

    }

}

