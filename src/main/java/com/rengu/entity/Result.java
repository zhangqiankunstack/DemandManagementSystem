package com.rengu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rengu.enums.SystemEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class Result<T> implements Serializable {

    private String id = UUID.randomUUID().toString();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();
    private int code;
    private String message;
    private T data;

    public Result(SystemEnum systemEnum, T data) {
        this.code = systemEnum.getCode();
        this.message = systemEnum.getMessage();
        this.data = data;
    }

    public Result(T data) {
        this.code = 1;
        this.message = "请求成功";
        this.data = data;
    }

}
