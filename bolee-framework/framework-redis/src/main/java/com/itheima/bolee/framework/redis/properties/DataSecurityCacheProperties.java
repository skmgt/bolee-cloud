package com.itheima.bolee.framework.redis.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName TenantProperties.java
 * @Description spring-cache多租户属性
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.cache")
public class DataSecurityCacheProperties {

    //需要忽略的数据权限的表
    public  List<String> ignoreDataSecurityTables;

}
