package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.vo.EntityInfo;
import com.rengu.entity.vo.EntityQueryVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @ClassName EntityService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
public interface EntityService extends IService<EntityModel> {

    List<EntityModel> connect(HostInfoModel hostInfo, String keyWord);

    Map<String, Object> getAllEntity(String keyWord, Integer pageNumber, Integer pageSize);

    Map<String,Object> findTrace(String type);

    EntityQueryVo queryEntities(List<String> entityIdList);

    List<Object> coverageAnalysisTrace();

    boolean deletedById(String id);

    void exportReport(List<String> base64List,String templateId,HttpServletResponse response);

    void exportSchemeAppraisal(String templateId, String filePath, String fileName, HttpServletResponse response);

    List<EntityModel> fetchUnrelatedEntities(List<String> ids);
}
