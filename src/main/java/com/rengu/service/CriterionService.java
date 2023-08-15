package com.rengu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.CriterionModel;
import com.rengu.entity.PersonnelModel;


/**
 * @ClassName CriterionService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/04 09:45
 **/
public interface CriterionService extends IService<CriterionModel> {
    Page<CriterionModel> page(Integer index, Integer size, String criterionName, String reviewPoints, String reviewCriteria, String reviewProcess);

}
