package com.rengu.controller;

import com.rengu.entity.HostInfoModel;
import com.rengu.entity.Result;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.service.RelationshipService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RelationshipController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@RestController
@RequestMapping("/relationship-model")
@Api(tags = "实体关联关系层-API")
public class RelationshipController {

    @Autowired
    public RelationshipService relationshipModelService;


    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public boolean remove(String Id) {
        return relationshipModelService.removeById(Id);
    }

    @ApiOperation(value = "查询元数据关联关系(物化采集)")
    @PostMapping("/findRelationshipInfo")
    public Result findRelationshipInfo(@RequestBody HostInfoModel hostInfo, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<EntityAndEntityVo> values = relationshipModelService.connect(hostInfo);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityAndEntityVo>().separatePageList(values, requestParams);
        return ResultUtils.build(requestParams);
    }

    @ApiOperation(value = "/根据实体id查询关联关系（物化采集）")
    @GetMapping("/findRelationshipByEntityId")
    public Result findRelationshipByEntityId(@RequestParam String entityId, @RequestParam(required = false)String keyWord,@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(relationshipModelService.findRelationshipByEntityId(entityId,keyWord, pageNumber,pageSize));
    }

    @ApiOperation("查询关联关系（本地数据）")
    @GetMapping("/getAllRelationship")
    public Result getAllRelationship(@RequestParam String entityId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(relationshipModelService.getAllRelationship(entityId, keyWord, pageNumber, pageSize));
    }

}
