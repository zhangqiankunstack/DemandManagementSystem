package com.rengu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.ValueModel;
import com.rengu.entity.vo.ValueAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ValueMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Mapper
public interface ValueMapper extends BaseMapper<ValueModel> {

    List<ValueAttribute> getAttributeAndValue(@Param("entityId") String entityId, @Param("attributeName") String attributeName, @Param("value") String value);


}
