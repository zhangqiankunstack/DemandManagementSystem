package com.rengu.entity.vo;

import com.rengu.entity.AttributeHistoryModel;
import com.rengu.entity.AttributeModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValueAttributeHistory extends AttributeHistoryModel{
    private String value;

    private AttributeHistoryModel attributeHistoryModel;
}
