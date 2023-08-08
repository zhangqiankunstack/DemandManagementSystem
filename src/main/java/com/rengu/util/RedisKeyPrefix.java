package com.rengu.util;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Version 1.0
 * @Description redis key前缀
 */
@Data
@Accessors(chain = true)
public class RedisKeyPrefix {

    /**
     * 元数据Entity
     */
    public static final String ENTITY = "ENTITY";

    /**
     * 元数据关联表RELATIONSHIP
     */
    public static final String RELATIONSHIP = "RELATIONSHIP";

    /**
     * 元数据属性ATTRIBUTE
     */
    public static final String ATTRIBUTE = "ATTRIBUTE";

    /**
     * 元数据属性VALUE
     */
    public static final String VALUE = "VALUE";
}
