package com.rengu.enums;

/**
 * TODO
 *
 * @author zqk
 * @version 1.0
 * @date 2020/11/3 14:56
 */
public enum SystemEnum {

    SUCCESS(0, "请求成功"),
    FAIL(1, "请求错误"),

    ROLE_NAME_IS_EMPTY(101, "不存在"),
    ROLE_IS_EXISTS(102, "已存在该角色"),
    ROLE_NOT_FOUND(103, "未发现该角色"),


    //公共指标管理
    TARGET_ARGS_NOT_NULL(10001, "公共指标内容不能为空"),
    TARGET_NAME_NOT_NULL(10002, "公共指标名称不能为空"),
    TARGET_ID_NOT_NULL(10003, "公共指标ID未发现"),


    //文件管理
    FILE_ARGS_NOT_NULL(40001, "文件不能为空"),
    TASK_NODE_ID_NOT_FOUND(40002, "流程节点ID未发现"),


    //节点管理
    TASK_NOT_ARGS_NOT_NULL(40001, "流程节点不能为空"),

    //专家管理
    EXPERT_ARGS_NOT_NULL(50001, "专家内容不能为空"),
    EXPERT_ID_NOT_FIND(50002, "未发现用户id"),

    //专家私有专家管理
    EXPERT_PRIVATE_ID_NOT_FOUND(70001, "专家私有专家ID未发现"),
    EXPERT_PRIVATE_ARGS_NOT_NULL(70002, "专家私有专家内容不能为空"),

    //数据库异常提示
    DBINFO_SQL_ID_NOT_FOUND(60001, "未发现数据库信息ID"),
    DATABASE_IS_NOT_CONNECTED(60002, "数据库未连接"),
    DATABASE_ARGS_IS_NULL(60003, "数据库信息为空"),

    //导入信息异常提示
    PLAN_ID_NOT_NULL(70001, "planId不可以为空"),

    //数据处理异常提示
    IMPORT_PLAN_DATA_PRO_NOT_FIND(80001, "Pro信息不存在"),
    PLEASE_IMPORT_OR_GENERATA_DATA_FIRST(80002,"请先导入或生成数据"),

    //EXTREMUM异常处理类
    EXTREMUM_NOT_FIND(90001, "EXTREMUM信息不存在");

    private int code;
    private String message;

    SystemEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
