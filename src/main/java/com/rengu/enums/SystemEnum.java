package com.rengu.enums;

/**
 * TODO
 *
 * @author yyc
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

    //私有指标管理
    TARGET_PRIVATE_ID_NOT_FOUND(11001, "工程指标ID不能为空"),
    TARGET_PRIVATE_NAME_EXIST(11002, "工程指标名称已存在"),
    TARGET_PRIVATE_NAME_NOT_NULL(11003, "工程指标名称不能为空"),

    //任务管理
    TASK_NAME_EXIST(20001, "任务名称已存在"),
    TASK_ARGS_NOT_NULL(20002, "任务内容不能为空"),
    TASK_ID_NOT_FOUND(20003, "任务ID不能为空"),
    TASK_ID_NOT_FIND(20004, "未发现任务ID"),
    TASK_NAME_IS_NULL(20005, "任务名称不存在"),
    TASK_VERSION_IS_NULL(20006, "任务版本不存在"),
    TASK_NAME_AND_VERSION_EXISTED(20007, "该任务已经存在"),
    TASK_NOT_FIND(20008, "未发现任务"),
    FLAG_NOT_FIND(20009, "未发现flag"),
    TASK_CLASS_NOT_FIND(20010, "任务标书不能为空"),
    ADD_INDICATORS_UNDER_THIS_TASK(20011, "该任务下未绑定指标"),
    NO_MAPPING_DATA_IS_IMPORTED_UNDER_THIS_TASK(20012, "该任务下未导入映射数据"),


    //工程管理
    PROJECT_ID_NOT_FOUND(30001, "工程ID未发现"),
    PROJECT_ARGS_NOT_NULL(30002, "工程内容不能为空"),
    PROJECT_NAME_EXIST(30003, "工程名称已存在"),
    PROJECT_NAME_NOT_NULL(30003, "工程名称不能为空"),

    //文件管理
    FILE_ARGS_NOT_NULL(40001, "文件不能为空"),
    TASK_NODE_ID_NOT_FOUND(40002, "流程节点ID未发现"),
    //节点管理
    TASK_NOT_ARGS_NOT_NULL(40001, "流程节点不能为空"),

    //专家管理
    EXPERT_ARGS_NOT_NULL(50001, "专家内容不能为空"),
    EXPERT_ID_NOT_FIND(50002, "未发现用户id"),

    //工程私有算法管理
    AlGORITHM_PRIVATE_NAME_EXIST(60001, "算法名称已存在"),
    AlGORITHM_PRIVATE_ID_NOT_FOUND(60002, "算法ID未发现"),
    AlGORITHM_PRIVATE_ARGS_NOT_NULL(60003, "算法内容不存在"),
    AlGORITHM_PRIVATE_FILE_ID_NOT_FOUND(610001, "私有算法文件ID未发现"),
    FILE_MD5_NOT_FOUND(610002, "未发现该文件MD5"),
    FILE_CHUNK_NOT_FOUND(610003, "未发现该文件块"),
    FILE_MD5_EXISTED(610004, "该文件MD5已存在"),
    FILE_ID_NOT_FOUND(610005, "未发现该文件Id"),
    AlGORITHM_PARAMETER_VALUE_NOT_SET(610006, "算法未设置参数值"),


    //专家私有专家管理
    EXPERT_PRIVATE_ID_NOT_FOUND(70001, "专家私有专家ID未发现"),
    EXPERT_PRIVATE_ARGS_NOT_NULL(70002, "专家私有专家内容不能为空"),

    //连线管理
    LINKS_ID_NOT_FOUND(80001, "连线ID未发现"),

    //sheet表
    SHEET_EXIST_NO_POINT(90001, "sheet表存在无效的打分"),

    //打分
    PLAN_ARGS_NOT_NUL(11000, "打分内容不能为空"),
    EXPERTINPORTANCE_ARGS_NOT_NULL(12000, "重要程度内容不能为空"),

    //用户信息提示
    USER_USERNAME_IS_EMPTY(201, "用户名参数不存在"),
    USER_PASSWORD_IS_EMPTY(202, "用户密码参数不存在"),
    USER_NAME_NOT_FOUND(205, "用户名不存在"),
    USER_NOT_FOUND(203, "未发现该用户"),
    USER_IS_EXISTS(204, "已存在该用户"),
    USER_ID_NOT_FOUND_ERROR(207, "用户id不存在"),
    USER_ROLE_NOT_FOUND_ERROR(10019, "请指定用户角色"),

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
