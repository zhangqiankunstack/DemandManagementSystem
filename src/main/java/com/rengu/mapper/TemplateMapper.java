package com.rengu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rengu.entity.TemplateModel;
import com.rengu.entity.vo.TemplateWithFileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TemplateMapper extends BaseMapper<TemplateModel> {

    @Select("SELECT t.id as templateId, t.template_name, t.description, f.id as fileId, f.file_name, f.local_path FROM template_model t LEFT JOIN file_model f ON t.file_id = f.id WHERE t.id = #{templateId}")
    TemplateWithFileVo getTemplateAndFileInformationByTemplateId(@Param("templateId") String templateId);

}
