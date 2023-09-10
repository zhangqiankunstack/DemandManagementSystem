package com.rengu.controller;

import com.rengu.entity.*;
import com.rengu.service.HostInfoService;
import com.rengu.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program
 * @Author Miracle ZHQK
 * @Description:数据配置管理
 * @Date 2023/08/07 15:22
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/dataConfiguration")
@Api(tags = "数据配置")
public class DataConfigurationController {

    @Autowired
    private HostInfoService hostInfoService;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("测试连接数据库")
    @PostMapping(value = "/testConnect")
    public Result testConnect(@RequestBody HostInfoModel dbInfo) {
        return ResultUtils.build(DataBaseFactoryService.getDbFactory().testConnect(dbInfo) ? "连接成功" : "连接失败");
    }

    @ApiOperation("保存数据库信息/修改数据库信息")
    @PostMapping(value = "/saveOrUpdateDbInfo")
    public Result saveOrUpdateDbInfo(@RequestBody HostInfoModel dbInfo) {
        return ResultUtils.build(hostInfoService.saveOrUpdateDbInfo(dbInfo));
    }

    @ApiOperation("根据数据库信息id删除信息")
    @DeleteMapping(value = "/{dbInfoId}/deletedDbInfoById")
    public Result deletedDbInfoById(@PathVariable(value = "dbInfoId") String dbInfoId) {
        return ResultUtils.build(hostInfoService.deletedDbInfoById(dbInfoId));
    }

    @ApiOperation(value = "通过数据库id查询出数据库信息")
    @GetMapping(value = "/{dbInfoId}/getDbInfo")
    public Result getDbInfo(@PathVariable(value = "dbInfoId") String dbInfoId) {
        return ResultUtils.build(hostInfoService.getDbInfoById(dbInfoId));
    }

    @ApiOperation("分页模糊查询数据配置")
    @GetMapping(value = "/getDbInfoList")
    public Result getDbInfoList(@RequestParam(required = false) String keyWord, @RequestParam() Integer pageNumber, @RequestParam() Integer pageSize) {
        return ResultUtils.build(hostInfoService.getDbInfoList(keyWord, pageNumber, pageSize));
    }

    /**
     * 保存元数据实体、关联关系、属性
     *
     * @param entityIds
     * @return
     */
    @ApiOperation(value = "保存元数据实体、关联关系、属性")
    @PostMapping("saveMetadata")
    public Result saveMetadata(@RequestBody List<String> entityIds) {
        List<EntityModel> entityModels = (List<EntityModel>) redisUtils.get(RedisKeyPrefix.ENTITY);
        List<RelationshipModel> relationshipModels = (List<RelationshipModel>) redisUtils.get(RedisKeyPrefix.RELATIONSHIP);
        List<AttributeModel> attributeModels = (List<AttributeModel>) redisUtils.get(RedisKeyPrefix.ATTRIBUTE);
        List<ValueModel> valueModels = (List<ValueModel>) redisUtils.get(RedisKeyPrefix.VALUE);
        return ResultUtils.build(hostInfoService.saveMetadata(entityModels, relationshipModels, attributeModels, valueModels, entityIds));
    }

    /**
     * 批量上传任务清单并解析
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "批量上传任务清单")
    @PostMapping(value = "importTaskXml")
    public Result importTaskFiles(@RequestParam(value = "files") List<MultipartFile> multipartFile) {
        return ResultUtils.build(hostInfoService.importTaskXml(multipartFile));
    }

    /**
     * 导出 方案评审证书
     * @param reviewId 评审ID
     * @param response
     * @return
     */
    @GetMapping("/exportSchemeAppraisal")
    public void exportSchemeAppraisal(String reviewId, HttpServletResponse response){
        String fileName = "ftl导出模板";
        String filePath = System.getProperty("os.name").startsWith("Windows") ? "D:/" : "/usr/etc";
        hostInfoService.exportSchemeAppraisal(reviewId,filePath,fileName,response);
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


    @GetMapping("/testFtl")
    public void testFtl(HttpServletResponse response){
        // 此条数据是通过数据库查询出的数据
//        User user = new User("李四",13,173,56);
//        Map<String,Object> map = new HashMap<>();
//
//        // 此数据是查询数据库返回的集合
//        List<User> userList = new ArrayList<>();
//        userList.add(new User("李四1",13,173,53));
//        userList.add(new User("李四2",14,174,54));
//        userList.add(new User("李四3",15,175,55));
//        userList.add(new User("李四4",16,176,56));
//        userList.add(new User("李四5",17,177,57));

        // 添加到map传给模板
//        map.put("user",user);
//        map.put("userList",userList);
//
//        FtlUtils.reportPeisOrgReservation(map,response);
    }
}
