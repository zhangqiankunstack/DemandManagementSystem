package com.rengu.entity.vo;

import lombok.Getter;

/**
 *
 * @author CuiYi cuiyi@zbitedu.cn
 * @version 1.0.0
 * @create 2022/10/19
 */
public class Result {
    @Getter
    private Boolean success;
    @Getter
    private String message;
    @Getter
    private Object data;
    @Getter
    private Integer code;

    private static final String DEFAULT_SUCCESS_MESSAGE = "OK";
    private static final String DEFAULT_FAIL_MESSAGE = "ERROR";
    private static final Integer DEFAULT_SUCCESS_CODE = 200;
    private static final Integer DEFAULT_FAIL_CODE = 500;

    private Result(Boolean success, String message, Object data, Integer code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public static Result success(Object data) {
        return success(data, DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result success(String message) {
        return success(null, message);
    }

    public static Result success(Object data, String message) {
        return new Result(true, message, data, DEFAULT_SUCCESS_CODE);
    }

    public static Result fail(String message) {
        return fail(message, DEFAULT_FAIL_CODE);
    }

    public static Result fail(String message, Integer code) {
        return new Result(false, message, null, code);
    }
}
