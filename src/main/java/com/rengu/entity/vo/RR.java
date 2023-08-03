package com.rengu.entity.vo;

import lombok.Getter;

/**
 * <一句话功能描述>
 *
 * @author CuiYi cuiyi@zbitedu.cn
 * @version 1.0.0
 * @create 2022/10/19
 */
public class RR {
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

    private RR(Boolean success, String message, Object data, Integer code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public static RR success(Object data){
        return success(data,DEFAULT_SUCCESS_MESSAGE);
    }

    public static RR success(String message){
        return success(null,message);
    }

    public static RR success(Object data ,String message){
        return new RR(true,message,data,DEFAULT_SUCCESS_CODE);
    }

    public static RR fail(String message){
        return fail(message,DEFAULT_FAIL_CODE);
    }

    public static RR fail(String message,Integer code){
        return new RR(false,message,null,code);
    }
}
