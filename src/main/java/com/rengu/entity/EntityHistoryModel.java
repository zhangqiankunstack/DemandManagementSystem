package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName EntityHistoryModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/08 14:36
 **/
@Data
    @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("entity_history")
@ApiModel(value = "EntityHistoryModel", description = "")
public class EntityHistoryModel implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "entity_historyid", type = IdType.UUID)
                private String entityHistoryid;

        private String entityId;

        private String version;

        private String entityName;

        @ApiModelProperty(value = "枚举：mission,activity,capability,function,system,node  没有默认，必填")
        private String entityType;


        }
