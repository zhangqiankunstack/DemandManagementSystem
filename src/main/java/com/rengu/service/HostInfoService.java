package com.rengu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HostInfoService
 * @Description 数据库表服务接口
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
public interface HostInfoService extends IService<HostInfoModel> {

    public List<ValueAttribute> findValueAttributesByEntityId(String entityId,String keyWord);


    List<EntityRelationship> getEntityRelationships(String entityId, String keyWord);


    void insertAll(String id);

    Integer deletedDbInfoById(String dbInfoId);

    HostInfoModel getDbInfoById(String dbInfoId);

    boolean saveOrUpdateDbInfo(HostInfoModel dbInfo);

    Object importTaskFiles(MultipartFile multipartFileList);

    List<EntityModel> saveMetadata(List<EntityModel> entity, List<RelationshipModel> relationship, List<AttributeModel> attributeModel, List<ValueModel> valueModels,List<String> entityIds);

    Map<String, Object> getDbInfoList(String keyWord, Integer pageNumber, Integer pageSize);
}


