package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rengu.entity.BaselineModel;
import com.rengu.entity.OpinionModel;
import com.rengu.entity.Result;
import com.rengu.entity.ReviewModel;
import com.rengu.entity.vo.EntityHistoryRelationship;
import com.rengu.entity.vo.EntityInfo;
import com.rengu.service.BaselineService;
import com.rengu.service.EntityBaselineService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BaselineController
 * @Description 基线控制器
 * @Author zj
 * @Date 2023/08/15 16:46
 **/
@RestController
@RequestMapping("/baseline-model")
@Api(value = "BaselineController", tags = {"基线控制器"})
public class BaselineController {

    @Autowired
    public BaselineService baselineModelService;
    @Autowired
    private EntityBaselineService entityBaselineService;
    @ApiOperation(value = "分页组合查询流程表")
    @GetMapping("/listByNameAndDescription")
    public Result testOnline(@RequestParam Integer pageNumber,
                             @RequestParam Integer pageSize,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String description) {
        List<BaselineModel> list = baselineModelService.findBaselineModelByNameAndDescription(name, description);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<BaselineModel>().separatePageList(list, requestParams);
        return ResultUtils.build(requestParams);
    }

    @PostMapping("/save")
    @ApiOperation(value = "点击添加，添加基线")
    public Result save(@RequestParam String baselineName, @RequestParam String baselineDescription, @RequestParam Integer priority) {

        baselineModelService.addBaseline(baselineName, baselineDescription, priority);
        return ResultUtils.build("ok");

    }

    @PostMapping("/update")
    @ApiOperation(value = "点击修改，修改基线")
    public Result update(@RequestParam Integer id, @RequestParam String baselineName, @RequestParam String baselineDescription, @RequestParam Integer priority) {

        baselineModelService.updateBaseline(id, baselineName, baselineDescription, priority);
        return ResultUtils.build("ok");

    }


    @ApiOperation(value = "查询流程表")
    @GetMapping("/list")
    public Result list(Integer id) {
        BaselineModel byId = baselineModelService.getById(id);
        return ResultUtils.build(byId);
    }


    @ApiOperation(value = "查询流程表-实体属性")
    @GetMapping("/listForInfo")
    public Result listForInfo(@RequestParam Integer id,@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

        List<EntityInfo> entityInfoByBaselineId = entityBaselineService.findEntityInfoByBaselineId(id);


        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityInfo>().separatePageList(entityInfoByBaselineId, requestParams);
        return ResultUtils.build(requestParams);


    }

    @ApiOperation(value = "下载")
    @GetMapping("/download")
    public Result download(Integer id, HttpServletResponse response) {
        List<EntityInfo> entityInfoByBaselineId = entityBaselineService.findEntityInfoByBaselineId(id);
        String fileName = "file.json"; // 下载文件的名称

        String str = new Gson().toJson(entityInfoByBaselineId);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(entityInfoByBaselineId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(json.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.build(json);
    }
}
