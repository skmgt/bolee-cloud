package com.itheima.bolee.framework.freemaker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * 用于操作freemaker的模板对象
 */
@Component
public class FreemakerTemplate {

    @Autowired
    private Configuration configuration;

    @Value("${spring.freemarker.template-name:defaultTemplate}")
    private String templateName;

    /**
     * 基于freemarker替换模板内容
     * @param templateName   模板名称
     * @param dateModel      数据模型
     * @param writer         替换后内容输出流
     * @throws IOException   未发现模板异常
     * @throws TemplateException  模板解析替换异常
     */
    public void replaceAndWriter(String templateName, Map dateModel, Writer writer) throws IOException, TemplateException {
        // 获取模板信息
        Template template = configuration.getTemplate(templateName);
        // 替换数据
        template.process(dateModel,writer);
    }

    /**
     * 基于freemarker替换模板内容
     * templateName   (spring.freemarker.template-name) 配置模板名称
     * @param dateModel      数据模型
     * @param writer         替换后内容输出流
     * @throws IOException   未发现模板异常
     * @throws TemplateException  模板解析替换异常
     */
    public void replaceAndWriter(Map dateModel, Writer writer) throws IOException, TemplateException {
        replaceAndWriter(templateName,dateModel,writer);
    }

    /**
     * @param templateName
     * @param dateModel
     * @return String 模板替换后的内容
     * @throws IOException
     * @throws TemplateException
     */
    public String replaceAndGetStr(String templateName, Map dateModel) throws IOException, TemplateException {
        // 定义用于存储替换后的输出数据
        StringWriter writer = new StringWriter();
        replaceAndWriter(templateName,dateModel,writer);
        return writer.toString();
    }
    /**
     * @param dateModel
     * @return String 模板替换后的内容
     * @throws IOException
     * @throws TemplateException
     */
    public String replaceAndGetStr(Map dateModel) throws IOException, TemplateException {
        return replaceAndGetStr(templateName,dateModel);
    }
    /**
     * @param dateModel
     * @return InputStream 模板替换后的内容对应的输入流 可以直接上传其它存储结构
     * @throws IOException
     * @throws TemplateException
     */
    public InputStream replaceAndGetInput(String templateName, Map dateModel) throws IOException, TemplateException {
        String content = replaceAndGetStr(templateName, dateModel);
        return new ByteArrayInputStream(content.getBytes());
    }
    /**
     * @param dateModel
     * @return InputStream 模板替换后的内容对应的输入流 可以直接上传其它存储结构
     * @throws IOException
     * @throws TemplateException
     */
    public InputStream replaceAndGetInput( Map dateModel) throws IOException, TemplateException {
        return replaceAndGetInput(templateName,dateModel);
    }

    public byte[] replaceAndGetBytes(String templateName, Map dateModel) throws IOException, TemplateException {
        String content = replaceAndGetStr(templateName, dateModel);
        return content.getBytes();
    }
    public byte[] replaceAndGetBytes( Map dateModel) throws IOException, TemplateException {
       return replaceAndGetBytes(templateName,dateModel);
    }
}
