package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.ValueHistoryModel;
import com.rengu.entity.ValueModel;
import com.rengu.mapper.AttributeHistoryMapper;
import com.rengu.mapper.ValueHistoryMapper;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.ValueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ValueHistoryServiceImpl
 * @Description 服务接口实现
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@Service
public class ValueHistoryServiceImpl extends ServiceImpl<ValueHistoryMapper, ValueHistoryModel> implements ValueHistoryService {

    @Autowired
    private ValueMapper valueMapper;

    @Autowired
    private AttributeHistoryMapper attributeHistoryMapper;

    @Override
    public void copyDataToValueHistory(List<String> valueIds) {
        // 查询第一个表的数据
        List<ValueModel> values = valueMapper.selectBatchIds(valueIds);

        // 遍历第一个表的数据，将其添加到第二个表中
        for (ValueModel value : values) {
            ValueHistoryModel valueHistory = new ValueHistoryModel();
            valueHistory.setValueId(value.getValueId());
            valueHistory.setValue(value.getValue());
            valueHistory.setAttributeId(value.getAttributeId());
            valueHistory.setEntityHistoryid(value.getEntityId());
            baseMapper.insert(valueHistory);

        }
    }

    @Override
    public void deleteByEntityHisId(String entityHisId) {
        List<ValueHistoryModel> allByEntityHisId = getAllByEntityHisId(entityHisId);
        allByEntityHisId.stream().forEach(valueHistory -> {
            if (valueHistory.getAttributeId() != null) {
                attributeHistoryMapper.deleteById(valueHistory.getAttributeId());
            }
            this.removeById(valueHistory.getValueId());
        });
//        this.removeByIds(allByEntityHisId);
    }

    /**
     * 通过历史id获取values
     *
     * @param entityHisId
     * @return
     */
    public List<ValueHistoryModel> getAllByEntityHisId(String entityHisId) {
        LambdaQueryWrapper<ValueHistoryModel> lambda = new LambdaQueryWrapper<>();
        lambda.eq(ValueHistoryModel::getEntityHistoryid, entityHisId);
        return this.list(lambda);
    }
}
