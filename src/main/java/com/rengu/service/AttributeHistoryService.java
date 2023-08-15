package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.AttributeHistoryModel;

import java.util.List;


/**
 * @ClassName AttributeHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
public interface AttributeHistoryService extends IService<AttributeHistoryModel> {
        public void copyDataToAttributeHistory(List<String> ids);
        }
