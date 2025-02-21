package com.itheima.bolee.framework.redis.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

/**
 * @ClassName TenantProperties.java
 * @Description spring-cache多租户属性
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.cache")
public class TenantCacheProperties {

    //需要忽略的企业ID的表
    public  List<String> ignoreCompanyTables;

    //默认企业编号Id
    public  String defaultCompanyNo;

}
