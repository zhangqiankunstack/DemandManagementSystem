package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
/**
 * 实体与实体关系处理返回结果（本地数据处理数据）
 */
public class EntityRelationship {

    private String relationshipId;

    private String entityName1;
    private String entityId1;
    private String entityName2;
    private String entityId2;
}
