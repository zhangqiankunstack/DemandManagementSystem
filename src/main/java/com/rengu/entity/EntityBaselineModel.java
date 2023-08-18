package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName EntityBaselineModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/16 19:29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("entity_baseline")
@ApiModel(value = "EntityBaselineModel", description = "")
public class EntityBaselineModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String entityHistoryid;

    private Integer baselineId;


}
