package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityModel;
import com.rengu.entity.OpinionModel;
import com.rengu.entity.ReviewModel;
import com.rengu.entity.vo.FourClasses;
import com.rengu.entity.vo.ValueAttribute;

import java.util.List;


/**
 * @ClassName ReviewService
 * @Description 流程表服务接口
 * @Author zj
 * @Date 2023/08/08 13:30
 **/
public interface ReviewService extends IService<ReviewModel> {

    ReviewModel add(String name, String sponsor, List<String> list);


    public List<ReviewModel> findBy(String name, String type, Integer status);


    Boolean goBy(List<String> ids);


    Boolean updateStatusById(String id, Integer status);

    public Object findEntitiesWithoutRelationship(List<String> entityIds, String name, String sponsor);


    List<EntityModel> findStart(String id);


    List<ValueAttribute> getAttributeAndValue(String entityId, String attributeName, String value);


    /**
     * 给前端四个实体
     *
     * @return
     */
    List<FourClasses> giveSome();


    Object saveStatusAndReview(String id, Integer status, List<OpinionModel> dataList);


}
