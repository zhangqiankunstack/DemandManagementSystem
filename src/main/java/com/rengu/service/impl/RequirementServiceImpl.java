package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.RequirementModel;
import com.rengu.entity.Result;
import com.rengu.mapper.RequirementMapper;
import com.rengu.service.RequirementService;
import com.rengu.util.ResultUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, RequirementModel> implements RequirementService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${server.port}")
    private String port;

    @Value("${service.ip}")
    private String serviceIp;

    @Value("${service.port}")
    private String servicePort;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            // 将文件保存到服务器上指定的文件夹中
            String originalName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString().replace("-", "");
            String picNewName = fileName + originalName.substring(originalName.lastIndexOf("."));
            String uploadFilePath = uploadPath + File.separator + "files";

            File file = new File(uploadFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File storageFile = new File(uploadFilePath + File.separator + picNewName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), storageFile);
            return storageFile.toString();
        } catch (Exception e) {
            return "上传失败：" + e.getMessage();
        }
    }

    @Override
    public boolean saveRequirementModel(RequirementModel requirementModel) {
        RequirementModel requirementByEntityId = getRequirementByEntityId(requirementModel.getEntityId());
        if (requirementByEntityId != null) {
            requirementByEntityId.setFileContent(requirementModel.getFileContent());
            return this.saveOrUpdate(requirementByEntityId);
        } else {
            return this.saveOrUpdate(requirementModel);
        }
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
        LambdaQueryWrapper<RequirementModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(RequirementModel::getEntityId, entityId);
        return this.getOne(lambda);
    }

    @Override
    public Object uploadPic(MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                return "文件为空,请重新选择!";
            }
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            String orgName = multipartFile.getOriginalFilename();
            String prefixName = orgName.substring(0, orgName.lastIndexOf("."));
            String suffixName = orgName.substring(orgName.lastIndexOf("."));
            String fileName;

            if (orgName.contains(".")) {
                fileName = prefixName + "_" + System.currentTimeMillis() + suffixName;
            } else {
                return "上传图片格式错误,请重新选择！";
            }
            String savePath = file.getPath() + File.separator + fileName;
            File saveFile = new File(savePath);
            FileCopyUtils.copy(multipartFile.getBytes(), saveFile);

            // 返回给前端的图片保存路径；前台可以根据返回的路径拼接完整地址，即可在浏览器上预览该图片
            String filePath = "upload/avatar" + File.separator + fileName;

//            InetAddress localHost = InetAddress.getLocalHost();
//            String ipAddress = localHost.getHostAddress();
//            String path = ipAddress + ":" + port + File.separator + filePath;

            //修改为通过配置获取ip，需要获取到nginx的代理地址对用户提供访问
            String path = serviceIp + ":" + servicePort + File.separator + filePath;

            if (path.contains("\\")) {
                path = path.replace("\\", "/");
            }
            return "http://"+path;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public void deleteByEntityId(String entityId) {
        RequirementModel requirementByEntityId = getRequirementByEntityId(entityId);
        this.removeById(requirementByEntityId);
    }
}
