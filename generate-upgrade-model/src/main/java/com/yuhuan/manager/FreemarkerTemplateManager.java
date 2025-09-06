package com.yuhuan.manager;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class FreemarkerTemplateManager {
    private Configuration configuration;

    /**
     * 构造函数：初始化Freemarker配置
     */
    public FreemarkerTemplateManager() {
        // 初始化Freemarker配置
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setBooleanFormat("c");
        configuration.setDefaultEncoding("UTF-8");
        try {
            configuration.setSetting("output_encoding", "UTF-8");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 动态设置模板文件所在的目录
     * @param templateDir 模板文件所在的目录路径
     */
    public void setTemplateDirectory(String templateDir) {
        try {
            // 设置模板文件夹路径
            configuration.setDirectoryForTemplateLoading(new File(templateDir));
            System.out.println("Template directory set to: " + templateDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set template directory: " + templateDir, e);
        }
    }

    /**
     * 根据模板文件名和数据模型生成内容并输出到指定文件
     * @param templateName 模板文件名（如template.ftl）
     * @param data 数据模型（Map类型）
     * @param outputFilePath 输出文件路径
     */
    public void generateFile(String templateName, Map<String, Object> data, String outputFilePath) {
        try {
            // 获取模板文件
            Template template = configuration.getTemplate(templateName);

            // 创建输出文件
            File outputFile = new File(outputFilePath);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs(); // 如果输出目录不存在，创建目录
            }

            // 使用模板和数据生成文件
//            try (Writer out = new BufferedWriter(new FileWriter(outputFile))) {
//                template.process(data, out);
//            }

            // 使用模板和数据生成文件，指定 UTF-8 编码
            try (Writer out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile.toPath()), StandardCharsets.UTF_8))) {
                template.process(data, out);
            }

            System.out.println("File generated successfully: " + outputFilePath);

        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Failed to generate file", e);
        }
    }

    /**
     * 根据模板文件名和数据模型生成内容并返回为字符串
     * @param templateName 模板文件名
     * @param data 数据模型
     * @return 生成的内容字符串
     */
    public String generateContent(String templateName, Map<String, Object> data) {
        try {
            // 获取模板文件
            Template template = configuration.getTemplate(templateName);

            // 使用模板和数据生成内容
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();

        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Failed to generate content", e);
        }
    }

    public void setTemplateLoader(ClassTemplateLoader classTemplateLoader) {
        configuration.setTemplateLoader(classTemplateLoader);
    }
}
