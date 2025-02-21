package com.itheima.bolee.insurance.urule.action;

import cn.hutool.core.util.ObjectUtil;
import com.itheima.bolee.framework.rule.model.flow.ActionNode;
import com.itheima.bolee.framework.rule.model.flow.FlowAction;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.framework.rule.model.flow.ins.ProcessInstance;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import com.itheima.bolee.insurance.urule.worrypay.HandlerManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WorryFreeFlow
 *
 * @author: wgl
 * @describe: 流程节点执行器
 * @date: 2022/12/28 10:10
 */
@Component
@Slf4j
public class WorryFreeFlow implements FlowAction {

    @Override
    public void execute(ActionNode node, FlowContext context, ProcessInstance instance) {
        String name = node.getName();
        log.info("执行节点：{}", name);
        FlowNodeAction handler = HandlerManage.getHandler(name);
        if(ObjectUtil.isNotNull(handler)){
            handler.execute(context);
        }
    }
}
