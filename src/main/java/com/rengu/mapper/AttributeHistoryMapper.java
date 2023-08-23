package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.AttributeHistoryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName AttributeHistoryMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Mapper
public interface AttributeHistoryMapper extends BaseMapper<AttributeHistoryModel> {


    @Select("SELECT * FROM attribute_history WHERE attribute_id = #{attributeId}")
    AttributeHistoryModel findByAttributeId (String attributeId);

}
