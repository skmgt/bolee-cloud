package com.itheima.bolee.insurance.urule.anno;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * FlowName
 * @author: wgl
 * @describe: 流程名称注解
 * @date: 2023/10/30 15:31
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface FlowName {

    String value();
}
