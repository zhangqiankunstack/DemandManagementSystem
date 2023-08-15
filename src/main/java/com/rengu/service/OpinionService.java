package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.OpinionModel;

import java.util.List;


/**
 * @ClassName OpinionService
 * @Description 专家意见表服务接口
 * @Author zj
 * @Date 2023/08/11 17:23
 **/
public interface OpinionService extends IService<OpinionModel> {

    public void batchInsert(List<OpinionModel> dataList);

    List<OpinionModel> findByReviewId(Integer reviewId);
}
