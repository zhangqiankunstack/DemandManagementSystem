package com.rengu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.OpinionModel;
import com.rengu.mapper.OpinionMapper;
import com.rengu.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OpinionServiceImpl
 * @Description 专家意见表服务接口实现
 * @Author zj
 * @Date 2023/08/11 17:23
 **/
@Service
public class OpinionServiceImpl extends ServiceImpl<OpinionMapper, OpinionModel> implements OpinionService {
    @Autowired
    private OpinionMapper opinionMapper;

    @Override
    public void batchInsert(List<OpinionModel> dataList) {
        opinionMapper.insertBatch(dataList);
    }

    @Override
    public List<OpinionModel> findByReviewId(Integer reviewId) {
        return opinionMapper.findByReviewId(reviewId);
    }
}
