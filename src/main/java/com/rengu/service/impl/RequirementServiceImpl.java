package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.RequirementModel;
import com.rengu.mapper.RequirementMapper;
import com.rengu.service.RequirementService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, RequirementModel> implements RequirementService {
    //    @Override
    public Object uploadPic1(MultipartFile multipartFile) {
        String fileSavePath = "D:\\报销\\壁纸\\";
        if (null == multipartFile || multipartFile.getSize() <= 0) {
            return new HashMap<String, Object>() {{
                put("code", 0);
                put("msg", "请选择上传文件。");
            }};
        }
        //文件名
        String originalName = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String picNewName = fileName + originalName.substring(originalName.lastIndexOf("."));
        String imgRealPath = fileSavePath + picNewName;
        try {
            //保存图片-将multipartFile对象装入image文件中
            File imageFile = new File(imgRealPath);
            multipartFile.transferTo(imageFile);

        } catch (Exception e) {
            return new HashMap<String, Object>() {{
                put("code", 0);
                put("msg", "图片保存异常:" + e);
            }};
        }
        return new HashMap<String, Object>() {{
            put("code", 0);
            put("msg", picNewName);
        }};
    }

    public String uploadPic(MultipartFile multipartFile) {
        try {
            // 将文件保存到服务器上指定的文件夹中
            String originalName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString().replace("-", "");
            String picNewName = fileName + originalName.substring(originalName.lastIndexOf("."));
            //todo:测试功能，临时路径写死，后期修改
            File storageFile = new File("D:\\报销\\壁纸\\" + picNewName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), storageFile);
            return storageFile.toString();
        } catch (Exception e) {
            return "上传失败：" + e.getMessage();
        }
    }

    @Override
    public boolean saveRequirementModel(RequirementModel requirementModel) {
        return this.saveOrUpdate(requirementModel);
    }

    /**
     * 根据id查询需求描述
     *
     * @param entityId
     * @return
     */
    @Override
    public RequirementModel getRequirementByEntityId(String entityId) {
        if (StringUtils.isEmpty(entityId)) {
            return null;
        }
        QueryWrapper<RequirementModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_id", entityId);
        RequirementModel requirementModel = getOne(queryWrapper);
        return requirementModel;
    }
}
