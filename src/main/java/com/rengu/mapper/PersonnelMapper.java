package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.PersonnelModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName PersonnelMapper
 * @Description 评审人员mapper接口
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
@Mapper
public interface PersonnelMapper extends BaseMapper<PersonnelModel> {

		}
