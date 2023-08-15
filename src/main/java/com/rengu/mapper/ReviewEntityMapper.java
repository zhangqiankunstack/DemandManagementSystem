package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.ReviewEntityModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName ReviewEntityMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/10 17:40
 **/
@Mapper
public interface ReviewEntityMapper extends BaseMapper<ReviewEntityModel> {
    List<String> selectByReviewId(Integer reviewId);
}
