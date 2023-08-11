package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.*;
import com.rengu.service.EntityService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName EntityController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@RestController
@RequestMapping("/entity-model")
@Api(tags = "实体控制层-API")
public class EntityController {

    @Autowired
    public EntityService entityModelService;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "展示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
    })
    public IPage<EntityModel> get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        return entityModelService.page(new Page<>(pageNum, pageSize));

    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public EntityModel get(String Id) {
        return entityModelService.getById(Id);
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public boolean remove(String Id) {
        return entityModelService.removeById(Id);
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public boolean saveOrUpdate(@RequestBody EntityModel entityModel) {
        return entityModelService.saveOrUpdate(entityModel);
    }

    /**
     * 采集任务展示实体、属性、关系（未入库数据接口）
     *
     * @param hostInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询元数据实体列表(物化采集)")
    @PostMapping("/findEntityModeList")
    public Result findEntityModeList(@RequestBody HostInfoModel hostInfo, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<EntityModel> entityModels = entityModelService.connect(hostInfo);
        if (entityModels.size() != 0) {
            redisUtils.set(RedisKeyPrefix.ENTITY, entityModels, 7200L);
        }
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityModel>().separatePageList(entityModels, requestParams);
        return ResultUtils.build(requestParams);
    }

    @ApiOperation("分页模糊查询实体列表（本地数据）")
    @GetMapping("/getAllEntity")
    public Result getAllEntity(@RequestParam(required = false) String keyWord,@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
        return ResultUtils.build(entityModelService.getAllEntity(keyWord,pageNumber,pageSize));
    }
}
