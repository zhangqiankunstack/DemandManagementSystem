package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.PersonnelModel;
import com.rengu.mapper.PersonnelMapper;
import com.rengu.service.PersonnelService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName PersonnelServiceImpl
 * @Description 评审人员服务接口实现
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
@Service
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, PersonnelModel> implements PersonnelService {

    @Override
    public Page<PersonnelModel> page(Integer index, Integer size, String name, String introduction) {
        Page<PersonnelModel> page = new Page<>();
        QueryWrapper<PersonnelModel> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (introduction != null && !introduction.isEmpty()) {
            queryWrapper.like("introduction", introduction);
        }
        queryWrapper.orderByAsc("display");
        List<PersonnelModel> data = baseMapper.page(page, queryWrapper);
        page.setRecords(data);

        return page;


    }
}
