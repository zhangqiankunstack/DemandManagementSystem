package com.rengu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.CriterionModel;
import com.rengu.entity.PersonnelModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CriterionMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/04 09:45
 **/
@Mapper
public interface CriterionMapper extends BaseMapper<CriterionModel> {


    List<CriterionModel> page(Page<CriterionModel> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
