package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName CriterionModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/04 09:45
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("criterion")
@ApiModel(value = "CriterionModel", description = "")
public class CriterionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String criterionName;

    private String reviewPoints;

    private String reviewCriteria;

    private String reviewProcess;

    private Integer display;


}
