package com.rengu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityBaselineModel;
import com.rengu.entity.vo.EntityInfo;
import com.rengu.mapper.EntityBaselineMapper;
import com.rengu.service.EntityBaselineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName EntityBaselineServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/16 19:29
 **/
@Service
		public class EntityBaselineServiceImpl extends ServiceImpl<EntityBaselineMapper, EntityBaselineModel>implements EntityBaselineService {


	@Autowired
	private EntityBaselineMapper entityBaselineMapper;
	@Override
	public List<EntityInfo> findEntityInfoByBaselineId(String baselineId) {
		List<EntityInfo> entityInfoByBaselineId = entityBaselineMapper.findEntityInfoByBaselineId(baselineId);
		return entityInfoByBaselineId;
	}
}
