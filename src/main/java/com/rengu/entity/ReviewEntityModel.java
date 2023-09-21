package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ReviewEntityModel
 * @Description 模型对象
 * @Author zj
 * @Date 2023/08/10 17:40
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("review_entity")
@ApiModel(value = "ReviewEntityModel", description = "")
public class ReviewEntityModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    private String reviewId;

    private String entityId;


}
