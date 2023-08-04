package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName AttributeModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@Data
    @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("attribute")
@ApiModel(value = "AttributeModel", description = "")
public class AttributeModel implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "attribute_id", type = IdType.UUID)
        private String attributeId;

        private String attributeName;


        }
