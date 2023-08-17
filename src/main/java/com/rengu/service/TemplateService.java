package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.TemplateModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TemplateService extends IService<TemplateModel> {
    boolean saveAndUploadFTL(TemplateModel templateModel, MultipartFile multipartFile);

    Map<String, Object> getAllTemplate(String keyWord, Integer pageNumber, Integer pageSize);

    boolean deleteTemplate(String templateId);
}
