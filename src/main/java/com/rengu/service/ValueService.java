package com.rengu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.ValueModel;

import java.util.List;


/**
 * @ClassName ValueService
 * @Description 服务接口
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
public interface ValueService extends IService<ValueModel> {

    List<ValueModel> connect(HostInfoModel hostInfo);
}
