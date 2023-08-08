package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.CriterionModel;
import com.rengu.entity.PersonnelModel;
import com.rengu.mapper.CriterionMapper;
import com.rengu.service.CriterionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CriterionServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/04 09:45
 **/
@Service
		public class CriterionServiceImpl extends ServiceImpl<CriterionMapper, CriterionModel>implements CriterionService {



	@Override
	public Page<CriterionModel> page(Integer index, Integer size,  String criterionName, String reviewPoints, String reviewCriteria, String reviewProcess) {
		Page<CriterionModel> page = new Page<>();
		QueryWrapper<CriterionModel> queryWrapper = new QueryWrapper<>();
		if (criterionName !=null && !criterionName.isEmpty()){
			queryWrapper.like("criterionName", criterionName);
		}
		if (reviewPoints !=null && !reviewPoints.isEmpty()){
			queryWrapper.like("reviewPoints", reviewPoints);
		}
		if (reviewCriteria !=null && !reviewCriteria.isEmpty()){
			queryWrapper.like("reviewCriteria", reviewCriteria);
		}
		if (reviewProcess !=null && !reviewProcess.isEmpty()){
			queryWrapper.like("reviewProcess", reviewProcess);
		}
		queryWrapper.orderByAsc("display");
		List<CriterionModel> data = baseMapper.page(page,queryWrapper);
		page.setRecords(data);
		return page;

	}
}
