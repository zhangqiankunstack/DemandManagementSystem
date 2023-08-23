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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        if(multipartFile!=null) {
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
        }
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
            if(fileModel!=null) {
                deletePathFile(fileModel.getLocalPath());
                fileService.removeById(fileModel.getId());
            }
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
}
