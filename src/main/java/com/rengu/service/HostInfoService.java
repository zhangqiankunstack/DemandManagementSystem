package com.rengu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @ClassName HostInfoService
 * @Description 数据库表服务接口
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
public interface HostInfoService extends IService<HostInfoModel> {
    /**
     * 数据库连接测试
     *
     * @param hostInfo
     * @return
     */
    public boolean databaseTest(HostInfoModel hostInfo);


    List<EntityModel> connect(HostInfoModel hostInfo);


    public List<ValueAttribute> findValueAttributesByEntityId(String entityId);


    List<EntityRelationship> getEntityRelationships(String entityId1);


    void insertAll(String id);

    Integer deletedDbInfoById(String dbInfoId);

    HostInfoModel getDbInfoById(String dbInfoId);

    Integer saveOrUpdateDbInfo(HostInfoModel dbInfo);

    Object importTaskFiles(MultipartFile multipartFileList);
}


