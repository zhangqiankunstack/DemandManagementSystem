package com.rengu.entity.vo;

import com.rengu.entity.EntityModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 覆盖分析追溯矩阵
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraceVo {
    /**
     * 系统、能力
     */
    private String entityName;

    /**
     * 系统功能、活动
     */
    List<EntityModel> entities = new ArrayList<>();
}
