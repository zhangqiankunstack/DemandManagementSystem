package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.EntityBaselineModel;
import com.rengu.entity.vo.EntityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName EntityBaselineMapper
 * @Description mapper接口
 * @Author zj
 * @Date 2023/08/16 19:29
 **/
@Mapper
public interface EntityBaselineMapper extends BaseMapper<EntityBaselineModel> {
	@Select("SELECT eh.entity_name AS entityName, eh.version ,eh.entity_type " +
			"FROM entity_baseline eb " +
			"JOIN entity_history eh ON eb.entity_historyid = eh.entity_historyid " +
			"WHERE eb.baseline_id = #{baselineId}")
	List<EntityInfo> findEntityInfoByBaselineId(@Param("baselineId") String baselineId);



	List<String> findEntityHistoryIdByBaseLineId(String id);


		}
