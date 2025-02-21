package com.itheima.bolee.insurance.urule.worrypay.handler;

import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SafeguardAnalysisStart
 *
 * @author: wgl
 * @describe: 评估保障开始
 * @date: 2022/12/28 10:10
 */
@FlowName(WorryFreeConstant.FLOW_NAME_SAFEGUARD_ANALYSIS_START)
@Slf4j
public class SafeguardAnalysisStart implements FlowNodeAction {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void execute(FlowContext flowContext) {
        String userId = String.valueOf(flowContext.getVariable("id"));
        // redis队列的key
        String listKey = WorryFreeConstant.WORRYFREE_PREFIX + userId;
        // 往队列中添加数据
        for (String index : WorryFreeConstant.getSafeguardFlowList()) {
            redissonClient.getList(listKey).add(index);
        }
    }
}
