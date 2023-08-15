package com.rengu.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理实体与实体之间关联信息展示（物化数据）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityAndEntityVo {

    /**
     * 实体1
     */
    private String entity1_name;

    /**
     * 实体2
     */
    private String entity2_name;

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 关联类型
     */
    private String relationship_type;
}
