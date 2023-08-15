package com.rengu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

/**
 * 模板管理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateModel {

    /**
     * 唯一标识
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 简介（描述）
     */
    private String description;

    /**
     * ftl文件标识
     */
    private String FileId;
}
