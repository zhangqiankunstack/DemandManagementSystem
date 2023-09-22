package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityBaselineModel;
import com.rengu.entity.vo.EntityInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @ClassName EntityBaselineService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/16 19:29
 **/
public interface EntityBaselineService extends IService<EntityBaselineModel> {

        List<EntityInfo> findEntityInfoByBaselineId(String baselineId);


}
