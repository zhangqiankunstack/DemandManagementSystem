package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class EntityHistoryRelationship {

    private String entityName;
    private String entityType;
    private String entityName1;
    private String entityName2;
    private String relationshipType;
}
