package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.BaselineModel;
import com.rengu.entity.EntityBaselineModel;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.ReviewModel;
import com.rengu.mapper.BaselineMapper;
import com.rengu.mapper.EntityBaselineMapper;
import com.rengu.mapper.EntityHistoryMapper;
import com.rengu.service.BaselineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BaselineServiceImpl
 * @Description 基线服务接口实现
 * @Author zj
 * @Date 2023/08/15 16:46
 **/
@Service
public class BaselineServiceImpl extends ServiceImpl<BaselineMapper, BaselineModel> implements BaselineService {
    @Autowired
    private EntityBaselineMapper entityBaselineMapper;

    @Autowired
    private EntityHistoryMapper entityHistoryMapper;



    @Override
    public List<BaselineModel> findBaselineModelByNameAndDescription(String name, String description) {

        QueryWrapper<BaselineModel> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("baseline_name", name);
        }
        if (description != null && !description.isEmpty()) {
            queryWrapper.like("baseline_description", description);
        }
        queryWrapper.orderByAsc("priority");

        return baseMapper.selectList(queryWrapper);

    }


    @Override
    public void addBaseline(String baselineName, String baselineDescription, Integer priority) {
        BaselineModel baseline = new BaselineModel();
        baseline.setBaselineName(baselineName);
        baseline.setBaselineDescription(baselineDescription);
        baseline.setCreatedTime(new Date());
        baseline.setPriority(priority);
        baseMapper.insert(baseline);

		List<EntityHistoryModel> entityHistoryModels = entityHistoryMapper.selectList(null);
		for (EntityHistoryModel entityHistoryModel : entityHistoryModels) {
			EntityBaselineModel entityBaselineModel = new EntityBaselineModel();
			entityBaselineModel.setEntityHistoryid(entityHistoryModel.getEntityHistoryid());
			entityBaselineModel.setBaselineId(baseline.getId());
			entityBaselineMapper.insert(entityBaselineModel);
		}

	}

    @Override
    public void updateBaseline(Integer id, String baselineName, String baselineDescription, Integer priority) {
        BaselineModel baseline = new BaselineModel();
        baseline.setId(id);
        baseline.setBaselineName(baselineName);
        baseline.setBaselineDescription(baselineDescription);
        baseline.setPriority(priority);
        baseline.setModifiedTime(new Date());

        baseMapper.updateById(baseline);
    }


}