package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EntityInfo {

    private String entityName;
    private String version;
    private String entityType;

}
