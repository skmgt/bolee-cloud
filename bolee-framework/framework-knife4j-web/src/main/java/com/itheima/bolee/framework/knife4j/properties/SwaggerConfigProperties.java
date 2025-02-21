package com.itheima.bolee.framework.knife4j.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @Description SwaggerConfigProperties配置类
 */

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "itheima.framework.swagger")
public class SwaggerConfigProperties implements Serializable {

    public String swaggerPath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;
}
