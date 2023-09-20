package com.rengu.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateWithFileVo {

    private String templateId;

    private String templateName;

    private String description;

    private String fileId;

    private String fileName;

    private String localPath;

}
