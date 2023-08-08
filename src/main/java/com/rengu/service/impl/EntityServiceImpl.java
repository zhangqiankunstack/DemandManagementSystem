package com.rengu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityModel;
import com.rengu.mapper.EntityMapper;
import com.rengu.service.EntityService;
import org.springframework.stereotype.Service;

/**
 * @ClassName EntityServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Service
public class EntityServiceImpl extends ServiceImpl<EntityMapper, EntityModel> implements EntityService {

}
