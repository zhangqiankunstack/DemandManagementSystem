package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName PersonnelModel
 * @Description 评审人员模型对象
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
@Data
    @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("personnel")
@ApiModel(value = "PersonnelModel", description = "评审人员")
public class PersonnelModel implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "id", type = IdType.AUTO)
                private Integer id;

        @ApiModelProperty(value = "专家姓名")
        private String name;

        @ApiModelProperty(value = "简介")
        private String introduction;

        @ApiModelProperty(value = "显示顺序")
        private Integer display;


        }
