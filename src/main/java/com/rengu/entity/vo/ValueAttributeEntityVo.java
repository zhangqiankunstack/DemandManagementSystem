package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValueAttributeEntityVo {
    private String attributeName;
    private String value;
    private String entityName;
}
