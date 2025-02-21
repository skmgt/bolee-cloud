package com.itheima.bolee.trade.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;

import java.util.*;

/**
 * @Description：代码生成器
 */
public class CodeGenerator {

    public static void autoGenerator(){

        //用来获取generrator.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("generrator");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String serviceProjectPath =rb.getString("serviceProjectPath");
        gc.setOutputDir(serviceProjectPath + "/src/main/java");
        gc.setAuthor(rb.getString("author"));
        gc.setOpen(false);
        gc.setFileOverride(true);
        //指定时间处理类型
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(rb.getString("url"));
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(rb.getString("userName"));
        dsc.setPassword(rb.getString("password"));
        mpg.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(rb.getString("moduleName"));
        pc.setParent(rb.getString("parent"));
        pc.setController("web");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("pojo");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //管理需要附件功能的代码生成
                List<String> tablesFile = Arrays.asList(rb.getString("tablesFile").split(","));
                Map<String, Object> map = new HashMap<>();
                map.put("tablesFile",tablesFile);
                this.setMap(map);
            }
        };

        String voProjectPath =rb.getString("voProjectPath");

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        if ("true".equals(rb.getString("face"))){
            String faceTemplatePath = rb.getString("face.ftl.path")+".ftl";
            // 自定义face配置会被优先输出
            focList.add(new FileOutConfig(faceTemplatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return serviceProjectPath + "/src/main/java/" +pc.getParent().replace(".","/")+"/face/"
                            + tableInfo.getEntityName() + "Face" + StringPool.DOT_JAVA;
                }
            });
        }

        if ("true".equals(rb.getString("faceImpl"))){
            String faceImplTemplatePath = rb.getString("faceImpl.ftl.path")+".ftl";
            // 自定义faceImpl配置会被优先输出
            focList.add(new FileOutConfig(faceImplTemplatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return serviceProjectPath + "/src/main/java/" +pc.getParent().replace(".","/")+"/face/impl/"
                            + tableInfo.getEntityName() + "FaceImpl" + StringPool.DOT_JAVA;
                }
            });
        }

        if ("true".equals(rb.getString("constant"))){
            String constantTemplatePath = rb.getString("constant.ftl.path")+".ftl";
            // 自定义constant配置会被优先输出
            focList.add(new FileOutConfig(constantTemplatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return voProjectPath + "/src/main/java/" +pc.getParent().replace(".","/")+"/constant/"
                            + tableInfo.getEntityName() + "CacheConstant" + StringPool.DOT_JAVA;
                }
            });
        }

        if ("true".equals(rb.getString("enums"))){
            String enumsTemplatePath = rb.getString("enums.ftl.path")+".ftl";
            // 自定义enums配置会被优先输出
            focList.add(new FileOutConfig(enumsTemplatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return voProjectPath + "/src/main/java/" +pc.getParent().replace(".","/")+"/enums/"
                            + tableInfo.getEntityName() + "Enum" + StringPool.DOT_JAVA;
                }
            });
        }

        if ("true".equals(rb.getString("vo"))){
            String voTemplatePath = rb.getString("vo.ftl.path")+".ftl";
            // 自定义Vo配置会被优先输出
            focList.add(new FileOutConfig(voTemplatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return voProjectPath + "/src/main/java/" +pc.getParent().replace(".","/")+"/vo/"
                            + tableInfo.getEntityName() + "Vo" + StringPool.DOT_JAVA;
                }
            });
        }


        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        if ("true".equals(rb.getString("entity"))){
            String entityFtlPath = rb.getString("entity.ftl.path");
            if (!EmptyUtil.isNullOrEmpty(entityFtlPath)){
                templateConfig.setEntity(entityFtlPath);
            }
        }else {
            templateConfig.setEntity(null);
        }

        if ("true".equals(rb.getString("mapper"))){
            String mapperFtlPath = rb.getString("mapper.ftl.path");
            if (!EmptyUtil.isNullOrEmpty(mapperFtlPath)){
                templateConfig.setMapper(mapperFtlPath);
            }
        }else {
            templateConfig.setMapper(null);
        }

        if ("true".equals(rb.getString("service"))){
            String serviceFtlPath = rb.getString("service.ftl.path");
            if (!EmptyUtil.isNullOrEmpty(serviceFtlPath)){
                templateConfig.setService(serviceFtlPath);
            }
        }else {
            templateConfig.setService(null);
        }

        if ("true".equals(rb.getString("serviceImpl"))){
            String serviceImpFtlPath = rb.getString("serviceImpl.ftl.path");
            if (!EmptyUtil.isNullOrEmpty(serviceImpFtlPath)){
                templateConfig.setServiceImpl(serviceImpFtlPath);
            }
        }else {
            templateConfig.setServiceImpl(null);
        }

        if ("true".equals(rb.getString("controller"))){
            String controllerFtlPath = rb.getString("controller.ftl.path");
            if (!EmptyUtil.isNullOrEmpty(controllerFtlPath)){
                templateConfig.setController(controllerFtlPath);
            }
        }else {
            templateConfig.setController(null);
        }
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(rb.getString("SuperEntityClass"));
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        String[] SuperEntityColumns = rb.getString("superEntityColumns").split(",");
        strategy.setSuperEntityColumns(SuperEntityColumns);
        strategy.setInclude(rb.getString("tableName").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        String tablePrefix = rb.getString("tablePrefix");
        if (tablePrefix!=null){
            strategy.setTablePrefix(tablePrefix);
        }
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
