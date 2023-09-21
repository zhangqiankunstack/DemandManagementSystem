package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName OpinionModel
 * @Description 专家意见表模型对象
 * @Author zj
 * @Date 2023/08/11 17:23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("opinion")
@ApiModel(value = "OpinionModel", description = "专家意见表")
public class OpinionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "专家名")
    private String name;

    @ApiModelProperty(value = "意见")
    private String expertOpinion;

    @ApiModelProperty(value = "状态:0为通过，1为驳回")
    private Integer status;

    @ApiModelProperty(value = "流程表")
    private Integer reviewId;


}
