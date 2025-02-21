package com.itheima.bolee.framework.gateway.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TenantProperties.java
 * @Description MyBaits-plus多租户属性
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "itheima.framework.log")
public class LogProperties {

    public List<String> ignoreUrl = new ArrayList<>();

}
