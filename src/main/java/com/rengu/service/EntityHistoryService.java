package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityHistoryModel;

import java.util.List;


/**
 * @ClassName EntityHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
public interface EntityHistoryService extends IService<EntityHistoryModel> {

    public void copyDataToEntityHistory(List<String> ids);


}
