package com.rengu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    /**
     * 唯一标识符
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

    /**
     * md5
     */
    private String md5;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件本地路径
     */
    private String localPath;
}
