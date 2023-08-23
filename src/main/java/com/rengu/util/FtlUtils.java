package com.rengu.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

@Component
@Log4j2
public class FtlUtils {

    private static final String ENCODING = "UTF-8";
    private static Configuration cfg = new Configuration();


    /**
     * 核心方法
     *
     * @param response 返回对象
     * @param data     渲染装载数据
     */
    public static void reportPeisOrgReservation(Map<String, Object> data, HttpServletResponse response) {
        try {
            reportPeisOrgReservation(data, "ftl模板.ftl", "logs/" + "aaa" + ".docx", "aaa" + ".docx", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 初始化cfg
    static {
        // 设置模板所在文件夹
        cfg.setClassForTemplateLoading(FtlUtils.class, "/template/");
//        cfg.setClassForTemplateLoading(FtlUtils.class, "/template/");
        // setEncoding这个方法一定要设置国家及其编码，不然在ftl中的中文在生成html后会变成乱码
        cfg.setEncoding(Locale.getDefault(), ENCODING);
        // 设置对象的包装器
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        // 设置异常处理器,这样的话就可以${a.b.c.d}即使没有属性也不会出错
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

    }

    // 获取模板对象
    public static Template getTemplate(String templateFileName) throws IOException {
        return cfg.getTemplate(templateFileName, ENCODING);
    }

    /**
     * 先根据模板生成word在本地,然后再上传到file文档服务器,最后删除本地word
     *
     * @param data             Map的数据结果集
     * @param templateFileName ftl模版文件名
     * @param outFilePath      生成文件名称(可带路径)
     * @throws Exception
     */
    public static void reportPeisOrgReservation(Map<String, Object> data, String templateFileName, String outFilePath, String fileName, HttpServletResponse response) throws Exception {
        Writer out = null;
        File outFile = new File(outFilePath);
        try {
            // 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
            Template template = getTemplate(templateFileName);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new OutputStreamWriter(new FileOutputStream(outFile), ENCODING);
            //下载到本地
            template.process(data, out);
            out.flush();
            log.info("由模板文件" + templateFileName + "生成" + outFilePath + "成功.");
        } catch (Exception e) {
            log.error("由模板文件" + templateFileName + "生成" + outFilePath + "出错");
        } finally {
            out.close();
        }
        // 获取服务器本地的文件位置
        File file = new File(outFilePath);
        if (file.exists()) {
            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                // 清除buffer缓存
                response.reset();
                // 指定下载的文件名
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + fileName);
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                FileInputStream inputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(inputStream); //缓冲流加速读
                OutputStream outputStream = response.getOutputStream();
                bufferedOutputStream = new BufferedOutputStream(outputStream);  //缓冲流加速写
                int n;
                while ((n = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("文件在本地服务器不存在");
        }
    }
}
