package com.itheima.bolee.framework.mybatisplus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName DataSecurityProperties.java
 * @Description 数据权限配置
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus")
public class DataSecurityProperties {

    //需要忽略的企业表
    private List<String> ignoreDataSecurityTables;

}
