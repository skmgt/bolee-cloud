package com.itheima.bolee.insurance.urule.worrypay.handler;

import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.insurance.dto.WorryFreeFlowNodeVO;
import com.itheima.bolee.insurance.pojo.WorryFreeFlowNode;
import com.itheima.bolee.insurance.service.IWorryFreeFlowNodeService;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * InsuranceMatchEnd
 *
 * @author: wgl
 * @describe: 产品匹配结束
 * @date: 2022/12/28 10:10
 */
@FlowName(WorryFreeConstant.FLOW_NAME_PRODUCT_MATCH_END)
public class InsuranceMatchEnd implements FlowNodeAction {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private IWorryFreeFlowNodeService worryFreeFlowNodeService;

    @Override
    public void execute(FlowContext flowContext) {

        String userId = String.valueOf(flowContext.getVariable("id"));
        // redis队列的key
        String listKey = WorryFreeConstant.WORRYFREE_PREFIX + userId;
        // 往队列中添加数据
        redissonClient.getList(listKey).add(WorryFreeConstant.FLOW_NAME_PRODUCT_MATCH_END);
        //将缓存中的数据添加到数据库中并删除缓存
        RList<Object> list = redissonClient.getList(listKey);
        WorryFreeFlowNodeVO node = WorryFreeFlowNodeVO.builder()
                .customerId(Long.valueOf(userId))
                .nodes(JSONUtil.parse(list).toString())
                .dataState(SuperConstant.DATA_STATE_0).
                build();
        worryFreeFlowNodeService.save(node);
        //清理缓存
        RKeys keys = redissonClient.getKeys();
        keys.deleteByPattern("user_risk_scores:accident:"+userId);
        keys.deleteByPattern("user_risk_scores:medical:"+userId);
        keys.deleteByPattern("worryfree:"+userId);
    }
}