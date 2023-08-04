package com.rengu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.PersonnelModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName PersonnelMapper
 * @Description 评审人员mapper接口
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
@Mapper
public interface PersonnelMapper extends BaseMapper<PersonnelModel> {


	List<PersonnelModel> page(Page<PersonnelModel> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
