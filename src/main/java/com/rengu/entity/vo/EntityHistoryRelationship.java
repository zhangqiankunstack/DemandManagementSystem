package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class EntityHistoryRelationship {

    private String relationshipId;
    private String entityHistoryId;
    private String entityType;
    private String entityName1;
    private String entityId1;
    private String entityName2;
    private String entityId2;
    private String relationshipType;

}
