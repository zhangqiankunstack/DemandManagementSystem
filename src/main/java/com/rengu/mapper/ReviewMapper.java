package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.RelationshipModel;
import com.rengu.entity.ReviewModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName ReviewMapper
 * @Description 流程表mapper接口
 * @Author zj
 * @Date 2023/08/08 13:30
 **/
@Mapper
public interface ReviewMapper extends BaseMapper<ReviewModel> {


}
