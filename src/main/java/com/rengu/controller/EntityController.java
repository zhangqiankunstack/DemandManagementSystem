package com.rengu.controller;

import com.rengu.entity.*;
import com.rengu.entity.vo.FetchUnrelatedEntitiesQueryVo;
import com.rengu.service.EntityService;
import com.rengu.service.RequirementService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        List<EntityModel> entityModels = entityModelService.connect(hostInfo, keyWord);
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
    public Result getRequirementByEntityId(@RequestParam String entityId) {
        return ResultUtils.build(requirementService.getRequirementByEntityId(entityId));
    }

    /**
     * 上传图片，可通过网络地址访问图片
     *
     * @param multipartFile
     * @return
     */
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

    @ApiOperation("需求追溯矩阵")
    @GetMapping("/requirementTrace")
    public Result requirementTrace(@RequestParam String type) {
        return ResultUtils.build(entityModelService.findTrace(type));
    }

    @ApiOperation("覆盖分析追溯矩阵")
    @GetMapping("/coverageAnalysisTrace")
    public Result coverageAnalysisTrace() {
        return ResultUtils.build(entityModelService.coverageAnalysisTrace());
    }

    @ApiOperation("根据实体id删除实体信息以及关联关系")
    @DeleteMapping("/deletedById")
    public Result deletedById(@RequestParam String id) {
        return ResultUtils.build(entityModelService.deletedById(id));
    }

    @ApiOperation("根据实体ids获取无关联关系的实体")
    @PostMapping("/unrelated")
    public Result fetchUnrelatedEntities(@RequestBody FetchUnrelatedEntitiesQueryVo query){
        return ResultUtils.build(entityModelService.fetchUnrelatedEntities(query.getIds()));
    }

    @CrossOrigin(origins = "http://localhost",methods = {RequestMethod.POST})
    @ApiOperation("导出实体、追溯报告")
    @PostMapping("/exportReport")
    public void exportReport(@RequestBody List<String> base64List,@RequestParam String templateId, HttpServletResponse response) {
        entityModelService.exportReport(base64List,templateId, response);
    }

    @PostMapping("/exportReport1")
    public void exportSchemeAppraisal(@RequestParam String templateId, HttpServletResponse response){
        String fileName = "ftl导出模板";
        String filePath = System.getProperty("os.name").startsWith("Windows") ? "D:/" : "/usr/etc";
        entityModelService.exportSchemeAppraisal(templateId,filePath,fileName,response);
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 向客户端输出
            File file = new File(filePath+fileName+".doc");
            inputStream = new FileInputStream(file);
            // 设置请求头
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName.replaceAll(" ", "")+".doc").getBytes(), "iso8859-1"));
            outputStream = response.getOutputStream();
            byte [] buffer = new byte [1024];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        File file = new File(filePath+fileName+".doc");
        file.delete(); // 移除文件
    }
}
