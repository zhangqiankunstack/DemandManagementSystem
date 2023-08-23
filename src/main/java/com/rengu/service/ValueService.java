package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;
import com.rengu.entity.vo.ValueAttribute;

import java.util.List;
import java.util.Map;


/**
 * @ClassName ValueService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
public interface ValueService extends IService<ValueModel> {

    List<ValueAttribute> connect(HostInfoModel hostInfo);

    Map<String, Object> getAllValueInfo(String entityId, String keyWord, Integer pageNumber, Integer pageSize);

    Map<String, Object> findValueInfoByEntityId(String entityId, String keyWord, Integer pageNumber, Integer pageSize);

    void deleteByEntityId(String id);
}
