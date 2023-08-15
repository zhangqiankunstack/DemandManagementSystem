package com.rengu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.AttributeHistoryModel;
import com.rengu.entity.AttributeModel;
import com.rengu.mapper.AttributeHistoryMapper;
import com.rengu.mapper.AttributeMapper;
import com.rengu.service.AttributeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AttributeHistoryServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Service
public class AttributeHistoryServiceImpl extends ServiceImpl<AttributeHistoryMapper, AttributeHistoryModel> implements AttributeHistoryService {

    @Autowired
    private AttributeMapper attributeMapper;

    @Override
    public void copyDataToAttributeHistory(List<String> ids) {

        // 查询第一个表的数据
        List<AttributeModel> attributes = attributeMapper.selectBatchIds(ids);

        // 遍历第一个表的数据，将其添加到第二个表中
        for (AttributeModel attribute : attributes) {
            AttributeHistoryModel attributeHistory = new AttributeHistoryModel();
            attributeHistory.setAttributeName(attribute.getAttributeName());
            baseMapper.insert(attributeHistory);
        }
    }
}
