package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.OpinionModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName OpinionMapper
 * @Description 专家意见表mapper接口
 * @Author zj
 * @Date 2023/08/11 17:23
 **/
@Mapper
public interface OpinionMapper extends BaseMapper<OpinionModel> {



	void insertBatch(List<OpinionModel> opinionList);


	List<OpinionModel>  findByReviewId(Integer reviewId);
		}
