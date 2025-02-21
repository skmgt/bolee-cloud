package com.itheima.bolee.framework.rule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * UruleBeanInit
 *
 * @author: wgl
 * @describe: Urule配置类加载
 * @date: 2022/12/28 10:10
 */
@Configuration
@ImportResource({"classpath:urule-core-context.xml"})
@PropertySource(value = {"classpath:urule-core-context.properties"})
public class UruleBeanInit {
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setOrder(1);
        return configurer;
    }
}