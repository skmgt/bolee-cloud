package com.itheima.bolee.insurance.urule.worrypay;

import com.itheima.bolee.framework.rule.model.flow.FlowAction;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * HandlerManage
 *
 * @author: wgl
 * @describe: 省心配流程节点管理器
 * @date: 2022/12/28 10:10
 */
@Component
public class HandlerManage implements ApplicationContextAware {

    //策略Map
    private static Map<String,FlowNodeAction> strategyMap = new HashMap<>();


    /**
     * 策略类初始化  key为自定义注解中的value  value为对应的策略类
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有的Handler
        Map<String, FlowNodeAction> beans = applicationContext.getBeansOfType(FlowNodeAction.class);
        beans.values().forEach(e->{
            FlowName flowName = e.getClass().getAnnotation(FlowName.class);
            if(flowName != null){
                //将注解中的value作为key，策略类作为value
                strategyMap.put(flowName.value(),e);
            }
        });
    }

    /**
     * 获取Handler的方法
     * @param name
     * @return
     */
    public static FlowNodeAction getHandler(String name) {
        return strategyMap.get(name);
    }
}
