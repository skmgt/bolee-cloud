package com.itheima.bolee.framework.mybatisplus.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName TenantProperties.java
 * @Description MyBaits-plus多租户属性
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "mybatis-plus")
public class TenantProperties {

    //需要忽略的企业表
    private List<String> ignoreCompanyTables;

    //默认企业编号Id
    private String defaultCompanyNo;

}
