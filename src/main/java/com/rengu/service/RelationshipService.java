package com.rengu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.RelationshipModel;
import com.rengu.entity.vo.EntityAndEntityVo;
import com.rengu.entity.vo.ValueAttribute;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RelationshipService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
public interface RelationshipService extends IService<RelationshipModel> {

    List<EntityAndEntityVo> connect(HostInfoModel hostInfo);

    Map<String, Object> getAllRelationship(String entityId, String keyWord, Integer pageNumber, Integer pageSize);
}
