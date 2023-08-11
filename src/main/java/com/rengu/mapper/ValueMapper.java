package com.rengu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.ValueModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ValueMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Mapper
public interface ValueMapper extends BaseMapper<ValueModel> {

}
