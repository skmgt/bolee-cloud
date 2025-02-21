package com.itheima.bolee.insurance.urule.worrypay;


import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;


/**
 * 流程节点动作接口
 */
public interface FlowNodeAction {


    public void execute(FlowContext flowContext);
}
