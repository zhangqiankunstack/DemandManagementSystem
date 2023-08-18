package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TwoValueAttributeEntityVo {

    private List<ValueAttributeEntityVo> valueList1;
    private List<ValueAttributeEntityVo> valueList2;
}
