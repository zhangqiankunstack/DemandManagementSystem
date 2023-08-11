package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;

import java.util.List;
import java.util.Map;


/**
 * @ClassName EntityService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
public interface EntityService extends IService<EntityModel> {

    List<EntityModel> connect(HostInfoModel hostInfo);

    Map<String, Object> getAllEntity(String keyWord,Integer pageNumber, Integer pageSize);
}
