package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ValueHistoryModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/08 14:37
 **/
@Data
    @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("value_history")
@ApiModel(value = "ValueHistoryModel", description = "")
public class ValueHistoryModel implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "value_id", type = IdType.UUID)
                private String valueId;

        private String entityHistoryid;

        private String attributeId;

        private String value;


        }
