package com.rengu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.vo.ValueAttribute;

import java.util.List;

/**
 * @ClassName HostInfoService
 * @Description 数据库表服务接口
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
public interface HostInfoService extends IService<HostInfoModel> {
        /**
         * 数据库连接测试
         * @param hostInfo
         * @return
         */
        public boolean databaseTest(HostInfoModel hostInfo);





        List<EntityModel> connect(HostInfoModel hostInfo);






        public List<ValueAttribute> findValueAttributesByEntityId(String entityId);




        }


