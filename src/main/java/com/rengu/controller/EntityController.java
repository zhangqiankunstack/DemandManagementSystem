package com.rengu.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rengu.entity.*;
import com.rengu.service.EntityService;
import com.rengu.service.RequirementService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private RequirementService requirementService;

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
    public Result findEntityModeList(@RequestBody HostInfoModel hostInfo, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<EntityModel> entityModels = entityModelService.connect(hostInfo,keyWord);
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
    public Result getAllEntity(@RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(entityModelService.getAllEntity(keyWord, pageNumber, pageSize));
    }

    @ApiOperation("根据实体id查询需求描述")
    @GetMapping("/getRequirementByEntityId")
    public Result getRequirementByEntityId(@RequestParam String entityId){
        return ResultUtils.build(requirementService.getRequirementByEntityId(entityId));
    }

    @ApiOperation("上传.md图片")
    @PostMapping("/uploadPicToServicePath")
    public Result uploadPicToServicePath(@RequestBody MultipartFile multipartFile) {
        return ResultUtils.build(requirementService.uploadPic(multipartFile));
    }

    @ApiOperation("保存md文件内容")
    @PostMapping("/saveRequirementModel")
    public Result saveRequirementModel(@RequestBody RequirementModel requirementModel) {
        return ResultUtils.build(requirementService.saveRequirementModel(requirementModel));
    }

    @ApiOperation("任务需求-能力需求追溯矩阵")
    @GetMapping("/missionAndCapabilityTrace")
    public Result missionAndCapabilityTrace(){
        return ResultUtils.build(entityModelService.missionAndCapabilityTrace());
    }
}
