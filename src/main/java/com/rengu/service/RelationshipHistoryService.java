package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.RelationshipHistoryModel;

import java.util.List;


/**
 * @ClassName RelationshipHistoryService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
public interface RelationshipHistoryService extends IService<RelationshipHistoryModel> {
    public void copyDataToRelationshipHistory(List<String> ids);

    void deleteByEntityHisId(String entityHisId);
}
