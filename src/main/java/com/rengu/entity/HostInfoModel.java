package com.rengu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * @ClassName HostInfoModel
 * @Description 数据库表模型对象
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("host_info")
@ApiModel(value = "HostInfoModel", description = "数据库表")
public class HostInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
//    @TableId(value = "id", type = IdType.AUTO)
    private String id = UUID.randomUUID().toString();

    @ApiModelProperty(value = "主机Ip")
    private String hostIp;

    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "数据库名")
    private String newDatabase;

    @ApiModelProperty(value = "数据库类型1：Mysql2：Oracle")
    private Integer status;

    @ApiModelProperty(value = "数据库类型：Mysql、Oracle......")
    private String dbType;

    @ApiModelProperty(value = "个人定义的数据库名")
    private String dbName;


}
