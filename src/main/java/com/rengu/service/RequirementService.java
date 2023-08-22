package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.RequirementModel;
import org.springframework.web.multipart.MultipartFile;

public interface RequirementService extends IService<RequirementModel> {
    String uploadFile(MultipartFile multipartFile);

    boolean saveRequirementModel(RequirementModel requirementModel);

    RequirementModel getRequirementByEntityId(String entityId);

    Object uploadPic(MultipartFile multipartFile);

    void deleteByEntityId(String id);
}
