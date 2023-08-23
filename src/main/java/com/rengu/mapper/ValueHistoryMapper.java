package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.ValueHistoryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ValueHistoryMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@Mapper
public interface ValueHistoryMapper extends BaseMapper<ValueHistoryModel> {

    @Select("SELECT * FROM value_history WHERE entity_historyid = #{entityId}")
    List<ValueHistoryModel> findValueHistoryModelByEntityId(String entityId);

}
