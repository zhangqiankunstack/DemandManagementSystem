package com.rengu.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class EntityRelationship {
    private String entityName;
    private String entityId2;
}
