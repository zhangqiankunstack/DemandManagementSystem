package com.rengu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.PersonnelModel;

import java.util.Date;


/**
 * @ClassName PersonnelService
 * @Description 评审人员服务接口
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
public interface PersonnelService extends IService<PersonnelModel> {

    Page<PersonnelModel> page(Integer index, Integer size, String name, String introduction);

}
