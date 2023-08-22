package com.rengu.controller;

import com.rengu.entity.Result;
import com.rengu.entity.TemplateModel;
import com.rengu.service.TemplateService;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 模板管理
 */
@RestController
@RequestMapping("/template")
@Api(tags = "模板管理")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    @ApiOperation("/保存模板、上传ftl文件")
    @PostMapping("/saveAndUploadFTL")
    public Result saveOrUpdate(TemplateModel templateModel, @RequestPart(required = false) MultipartFile multipartFile) {
        return ResultUtils.build(templateService.saveAndUploadFTL(templateModel, multipartFile));
    }

    @ApiOperation("模糊查询模板列表")
    @GetMapping("/getAllTemplate")
    public Result getAllTemplate(@RequestParam(required = false) String keyWord, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResultUtils.build(templateService.getAllTemplate(keyWord, pageNumber, pageSize));
    }

    @ApiOperation("根据id删除模板")
    @DeleteMapping("/deletedTemplateById")
    public Result deleteTemplate(@RequestParam String templateId) {
        return ResultUtils.build(templateService.deleteTemplate(templateId));
    }
}
