package com.rengu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HostInfoService
 * @Description 数据库表服务接口
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
public interface HostInfoService extends IService<HostInfoModel> {

    public List<ValueAttribute> findValueAttributesByEntityId(String entityId, String keyWord);

    List<EntityRelationship> getEntityRelationships(String entityId, String keyWord);

    void insertAll(String id);

    Integer deletedDbInfoById(String dbInfoId);

    HostInfoModel getDbInfoById(String dbInfoId);

    boolean saveOrUpdateDbInfo(HostInfoModel dbInfo);

    List<EntityModel> saveMetadata(List<EntityModel> entity, List<RelationshipModel> relationship, List<AttributeModel> attributeModel, List<ValueModel> valueModels, List<String> entityIds);

    Map<String, Object> getDbInfoList(String keyWord, Integer pageNumber, Integer pageSize);

    Object importTaskXml(List<MultipartFile> multipartFiles);

    /**
     * 导出 方案评审证书
     * @param reviewId 导出id
     * @param filePath 导出路径
     * @param fileName 导出文件名称
     * @param response
     */
    void exportSchemeAppraisal(String reviewId, String filePath, String fileName, HttpServletResponse response);
}


