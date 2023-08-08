package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName EntityModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/02 17:50
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("entity")
public class EntityModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "entity_id", type = IdType.UUID)
    private String entityId;
    private String entityName;

    private String entityType;


}
