package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.BaselineModel;
import com.rengu.entity.vo.ToJson;

import java.util.List;


/**
 * @ClassName BaselineService
 * @Description 基线服务接口
 * @Author zj
 * @Date 2023/08/15 16:46
 **/
public interface BaselineService extends IService<BaselineModel> {

        public List<BaselineModel> findBaselineModelByNameAndDescription(String name, String description);


        public void addBaseline(String baselineName, String baselineDescription,Integer priority);


        public void updateBaseline(Integer id, String baselineName, String baselineDescription, Integer priority);


        public ToJson allForDownload(Integer id);

        }
