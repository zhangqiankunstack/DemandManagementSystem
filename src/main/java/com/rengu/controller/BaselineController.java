package com.rengu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rengu.entity.BaselineModel;
import com.rengu.entity.Result;
import com.rengu.entity.vo.EntityInfo;
import com.rengu.entity.vo.ToJson;
import com.rengu.service.BaselineService;
import com.rengu.service.EntityBaselineService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
    public Result update(@RequestParam String id, @RequestParam String baselineName, @RequestParam String baselineDescription, @RequestParam Integer priority) {

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
    public Result listForInfo(@RequestParam Integer id, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

        List<EntityInfo> entityInfoByBaselineId = entityBaselineService.findEntityInfoByBaselineId(id);


        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<EntityInfo>().separatePageList(entityInfoByBaselineId, requestParams);
        return ResultUtils.build(requestParams);


    }

    @ApiOperation(value = "下载")
    @GetMapping("/download")
    public Result download(Integer id) throws JsonProcessingException {
        ToJson toJson = baselineModelService.allForDownload(id);
        ObjectMapper Obj = new ObjectMapper();

        String jsonStr = Obj.writeValueAsString(toJson);


        return ResultUtils.build(jsonStr);
    }

    @ApiOperation(value = "下载测试版")
    @GetMapping("/downloadTest")
    public Result downloadTest(Integer id) throws JsonProcessingException {
        ToJson toJson = baselineModelService.allForDownload(id);
//        ObjectMapper Obj = new ObjectMapper();
//
//        String jsonStr = Obj.writeValueAsString(toJson);


        return ResultUtils.build(toJson);
    }



//
//
//
//
//    @ApiOperation(value = "根据发布id下载文件")
//    @GetMapping(value = "/{toolPackageId}/exportToolPackage")
//    public void exportToolPackage(@PathVariable(value = "toolPackageId") Integer id, HttpServletResponse httpServletResponse) throws IOException {
//        //导出文件
//        String exportFileName = "yourFileName.extension";
//
////        File exportFile = toolPackageFileService.exportToolPackageFileByToolPackage(exportFileName);
//
//
//        String mimeType = URLConnection.guessContentTypeFromName(exportFile.getName()) == null ? "application/octet-stream" : URLConnection.guessContentTypeFromName(exportFile.getName());
//        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + new String(exportFile.getName().getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
//        httpServletResponse.setContentType(mimeType);
//        httpServletResponse.setContentLengthLong(exportFile.length());
//        // 文件流输出
//        IOUtils.copy(new FileInputStream(exportFile), httpServletResponse.getOutputStream());
//        httpServletResponse.flushBuffer();
//    }
//
//
//






}
