package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName RelationshipModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("relationship")
@ApiModel(value = "RelationshipModel", description = "")
public class RelationshipModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "relationship_id", type = IdType.UUID)
    private String relationshipId;

    private String entityId1;

    private String entityId2;

    @ApiModelProperty(value = "枚举：组成,依赖,影响,冲突,关联  默认是关联")
    private String relationshipType;


}
