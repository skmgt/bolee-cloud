package com.itheima.bolee.framework.rule.config;

import com.itheima.bolee.framework.rule.runtime.service.KnowledgeService;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * SpringApplicationAware
 *
 * @author: wgl
 * @describe: 上下文增强器
 * @date: 2022/12/28 10:10
 */
@Component
@Configuration
public class SpringApplicationAware implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }


    /**
     * 获取上下文中的内容
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return ctx;
    }
}
