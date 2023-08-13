package com.rengu.entity.vo;

import lombok.Data;

import java.util.List;

@Data
/**
 * 用于查询三张表的ids
 */
public class EntityQueryVo {
    private List<String> attributeIdList;
    private List<String> valueIdList;
    private List<String> relationshipIdList;
}
