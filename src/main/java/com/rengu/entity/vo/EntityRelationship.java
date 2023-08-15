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

    /**
     * 属性id
     */
    private String relationshipId;
    /**
     * 实体名称1
     */
    private String entityName1;
    /**
     * 实体id1
     */
    private String entityId1;
    /**
     * 实体名称2
     */
    private String entityName2;
    /**
     * 实体id2
     */
    private String entityId2;
    /**
     * 关联类型
     */
    private String relationshipType;
    /**
     * 关联类型
     */
    private String entityType;
}
