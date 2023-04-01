package club.doctorxiong.api.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 代码生成器代码
 *
 * @author xiongxin
 * @date 2021/5/14
 */
public class CodeGenerator {

    /**
     * tinyint
     */
    private static final String FIELD_TYPE_TINYINT = "tinyint";

    /**
     * 执行代码生成
     *
     * @param args args
     */
    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir")+"/src/main";

        FastAutoGenerator.create("jdbc:mysql://8.135.117.119:3306/miniprogress?serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&useSSL=false",
                "root", "Root_123456")
                .globalConfig(builder -> {
                    builder.author("xiongxin") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(projectPath + "/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("club.doctorxiong.miniprogress") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/resources/mapper/"));// 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableTableFieldAnnotation(); // 开启实体映射注解
                    builder.addInclude("daily_index_data"); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}