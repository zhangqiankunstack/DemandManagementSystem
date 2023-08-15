package com.rengu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;
import com.rengu.entity.Result;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.service.ValueService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ValueController
 * @Description 控制器
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@RestController
@RequestMapping("/value-model")
@Api(tags = "属性值控制层-API")
public class ValueController {

    @Autowired
    public ValueService valueModelService;

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public ValueModel get(String Id) {
        return valueModelService.getById(Id);
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public boolean remove(String Id) {
        return valueModelService.removeById(Id);
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public boolean saveOrUpdate(@RequestBody ValueModel valueModel) {
        return valueModelService.saveOrUpdate(valueModel);
    }

    /**
     * 采集任务展示属性值（未入库数据接口）
     *
     * @param hostInfo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询元数据属性值列表(物化采集)")
    @PostMapping("/findValueInfoList")
    public Result findValueInfoList(@RequestBody HostInfoModel hostInfo, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<ValueAttribute> values = valueModelService.connect(hostInfo);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<ValueAttribute>().separatePageList(values, requestParams);
        return ResultUtils.build(requestParams);
    }

    @ApiOperation(value = "根据实体id查询元数据属性(物化查询)")
    @GetMapping("/findValueInfoByEntityId")
    public Result findValueInfoByEntityId(@RequestParam String entityId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(valueModelService.findValueInfoByEntityId(entityId, keyWord, pageNumber, pageSize));
    }


    @ApiOperation("查询数据值列表（本地数据）")
    @GetMapping("/getAllValueInfo")
    public Result getAllValueInfo(@RequestParam String entityId, @RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(valueModelService.getAllValueInfo(entityId, keyWord, pageNumber, pageSize));
    }
}
