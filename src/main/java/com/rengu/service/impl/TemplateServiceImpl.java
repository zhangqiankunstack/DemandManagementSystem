package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.FileModel;
import com.rengu.entity.TemplateModel;
import com.rengu.mapper.TemplateMapper;
import com.rengu.service.FileService;
import com.rengu.service.RequirementService;
import com.rengu.service.TemplateService;
import com.rengu.util.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import java.util.*;

@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, TemplateModel> implements TemplateService {

    @Autowired
    private RequirementService requirementService;
    @Autowired
    private FileService fileService;

    @Transactional
    @Override
    public boolean saveAndUploadFTL(TemplateModel templateModel, MultipartFile multipartFile) {
        if (!StringUtils.isEmpty(templateModel.getFileId())) {//修改模板
            FileModel fileModel = fileService.getById(templateModel.getFileId());
            deletePathFile(fileModel.getLocalPath());
            fileService.removeById(fileModel.getId());
        }
        //上传文件
        String filePath = requirementService.uploadFile(multipartFile);
        FileModel file = new FileModel();
        String originalFilename = multipartFile.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        file.setFileName(split[0]);
        file.setSize(((multipartFile.getSize() / 1024) + 1) + "KB");
        file.setLocalPath(filePath);
        fileService.save(file);
        templateModel.setFileId(file.getId());
        return this.saveOrUpdate(templateModel);
    }

    /**
     * 模糊查询模板列表
     *
     * @param keyWord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> getAllTemplate(String keyWord, Integer pageNumber, Integer pageSize) {
        QueryWrapper<TemplateModel> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyWord)) {
            query.like("template_name", keyWord).or().like("description", keyWord);
        }
        List<TemplateModel> templateModels = this.list(query);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil().separatePageList(templateModels, requestParams);
        return requestParams;
    }

    /**
     * 根据id删除模板、模板文件
     *
     * @param templateId
     * @return
     */
    @Override
    public boolean deleteTemplate(String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            return false;
        }
        TemplateModel templateModel = this.getById(templateId);
        if (templateModel != null) {
            FileModel fileModel = fileService.getById(templateModel.getFileId());
            deletePathFile(fileModel.getLocalPath());
            fileService.removeById(fileModel.getId());
        }
        return this.removeById(templateId);
    }

    /**
     * 删除本地路径的ftl文件
     */
    public boolean deletePathFile(String path) {
        try {
            File file = new File(path);
            file.delete();
            System.out.println("文件删除成功！");
            return true;
        } catch (Exception e) {
            System.out.println("文件删除失败：" + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) throws MalformedURLException {
        getImage();
    }

    public static ResponseEntity getImage()throws MalformedURLException {
        String sourceFilePath = "D:\\报销\\壁纸\\0e45c74a9f2aeb65c56dc7b1f5340ee3.jpeg";  // 源图片文件路径
        String destinationDirectory = "D:\\报销\\壁纸\\"; // 目标文件夹路径
        String fileName = "saved_image.jpg"; // 保存的文件名

        Path imagePath = Paths.get(destinationDirectory, fileName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
