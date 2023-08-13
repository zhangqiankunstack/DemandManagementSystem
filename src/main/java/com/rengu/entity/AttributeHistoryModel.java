package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName AttributeHistoryModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Data
    @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("attribute_history")
@ApiModel(value = "AttributeHistoryModel", description = "")
public class AttributeHistoryModel implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "attribute_id", type = IdType.UUID)
                private String attributeId;

        private String attributeName;


        }
