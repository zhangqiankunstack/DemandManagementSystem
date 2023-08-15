package com.rengu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 需求描述（临时需求-需求描述（.md文件））
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequirementModel implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

    /**
     * .md文件内容
     */
    private String fileContent;

    /**
     * 实体id
     */
    private String entityId;
}
