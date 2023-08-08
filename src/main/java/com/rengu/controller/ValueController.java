package com.rengu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;
import com.rengu.entity.vo.Result;
import com.rengu.service.ValueService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.RedisKeyPrefix;
import com.rengu.util.RedisUtils;
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
@Api(tags = "控制器")
public class ValueController {

    @Autowired
    public ValueService valueModelService;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "展示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
    })
    public IPage<ValueModel> get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        return valueModelService.page(new Page<>(pageNum, pageSize));

    }

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
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询元数据属性值")
    @PostMapping("/getValueInfo")
    public Result getValueInfo(@RequestBody HostInfoModel hostInfo, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        List<ValueModel> values = valueModelService.connect(hostInfo);
        if (values.size() > 0) {
            redisUtils.set(RedisKeyPrefix.VALUE, values, 7200L);
        }
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNum);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<ValueModel>().separatePageList(values, requestParams);
        return Result.success(requestParams);
    }
}
