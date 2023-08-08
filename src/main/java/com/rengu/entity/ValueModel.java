package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ValueModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/02 18:03
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("value")
@ApiModel(value = "ValueModel", description = "")
public class ValueModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "value_id", type = IdType.UUID)
    private String valueId;

    private String entityId;

    private String attributeId;

    private String value;


}
