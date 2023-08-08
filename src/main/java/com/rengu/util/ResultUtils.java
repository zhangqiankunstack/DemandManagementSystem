package com.rengu.util;

import com.rengu.entity.Result;
import com.rengu.enums.SystemEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ResultUtils
 * @Description TODO
 * @Author yyc
 * @Date 2020/11/18 13:43
 * @Version 1.0
 */
@Slf4j
public class ResultUtils {

    public static Result build(Object data) {
        return new Result<>(SystemEnum.SUCCESS, data);
    }

    public static Result warn(SystemEnum resultCode, String msg) {
        Result<Object> result = new Result<>(resultCode);
        result.setMessage(msg);
        return new Result(resultCode);
    }
}
