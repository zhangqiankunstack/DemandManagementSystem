package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.ValueHistoryModel;

import java.util.List;
import java.util.Map;


/**
 * @ClassName ValueHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
public interface ValueHistoryService extends IService<ValueHistoryModel> {
    public void copyDataToValueHistory(List<String> valueIds);

    Map<String, Object> getAllValueInfo(String entityId, String keyWord, Integer pageNumber, Integer pageSize);


}
