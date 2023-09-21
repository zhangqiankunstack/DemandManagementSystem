package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ReviewModel
 * @Description 流程表模型对象
 * @Author zj
 * @Date 2023/08/08 13:30
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("review")
@ApiModel(value = "ReviewModel", description = "流程表")
public class ReviewModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "发起人")
    private String sponsor;

    @ApiModelProperty(value = "类型，本次评审的类型分别为增量，变更")
    private String type;

    @ApiModelProperty(value = "0为未处理，1为通过，2为未通过")
    private Integer status;


}
