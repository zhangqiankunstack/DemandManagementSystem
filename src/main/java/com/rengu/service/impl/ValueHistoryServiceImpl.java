package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.ValueHistoryModel;
import com.rengu.entity.ValueModel;
import com.rengu.mapper.AttributeHistoryMapper;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.entity.vo.ValueAttributeHistory;
import com.rengu.mapper.ValueAttributeHistoryMapper;
import com.rengu.mapper.ValueHistoryMapper;
import com.rengu.mapper.ValueMapper;
import com.rengu.service.ValueHistoryService;
import com.rengu.util.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ValueAttributeHistoryMapper valueAttributeHistoryMapper;

    @Autowired
    private AttributeHistoryMapper attributeHistoryMapper;

    @Override
    public void copyDataToValueHistory(List<String> valueIds, Map<String, EntityHistoryModel> entityHistoryMap) {
        // 查询第一个表的数据
        List<ValueModel> values = valueMapper.selectBatchIds(valueIds);

        // 遍历第一个表的数据，将其添加到第二个表中
        for (ValueModel value : values) {
            //获取到entityHistory
            EntityHistoryModel entityHistory = entityHistoryMap.get(value.getEntityId());
            ValueHistoryModel valueHistory = new ValueHistoryModel();
            valueHistory.setValue(value.getValue());
            valueHistory.setAttributeId(value.getAttributeId());
//            valueHistory.setEntityHistoryid(value.getEntityId());
            //存下EntityHistory的id，而不是entiyId
            valueHistory.setEntityHistoryid(entityHistory.getEntityHistoryid());
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

    @Override
    public Map<String, Object> getAllValueInfo(String entityId, String keyWord, Integer pageNumber, Integer pageSize) {
        List<ValueAttributeHistory> valueAttributeHistory = valueAttributeHistoryMapper.findValueAttributesByEntityId(entityId, keyWord);
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        new ListPageUtil<ValueAttributeHistory>().separatePageList(valueAttributeHistory, requestParams);
        return requestParams;
    }
}
