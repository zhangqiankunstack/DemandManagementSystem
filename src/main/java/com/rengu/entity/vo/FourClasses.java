package com.rengu.entity.vo;

import com.rengu.entity.CriterionModel;
import com.rengu.entity.OpinionModel;
import com.rengu.entity.PersonnelModel;
import com.rengu.entity.ReviewModel;
import lombok.Data;

import java.util.List;

@Data
public class FourClasses {
    /**
     * 准测
     */
    private List<CriterionModel>  criterionModelList;
    /**
     * 流程表里数据
     */
    private ReviewModel reviewModelList;
    /**
     * 人员
     */
    private List<PersonnelModel> personnelModelList;
    /**
     * 意见
     */
    private List<OpinionModel> opinionModelList;
}
