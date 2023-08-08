package com.rengu.entity.vo;

import com.rengu.entity.AttributeModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValueAttribute extends AttributeModel {

    private String value;

    private AttributeModel attribute;

}
