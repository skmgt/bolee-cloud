package com.itheima.bolee.insurance.urule.worrypay.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.insurance.dto.WorryFreeRiskItemVO;
import com.itheima.bolee.insurance.pojo.Risk;
import com.itheima.bolee.insurance.pojo.WorryFreeRiskItem;
import com.itheima.bolee.insurance.service.IRiskService;
import com.itheima.bolee.insurance.service.IWorryFreeRiskItemService;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RiskAnalysisEnd
 *
 * @author: wgl
 * @describe: 风险分析结束
 * @date: 2022/12/28 10:10
 */
@FlowName(WorryFreeConstant.FLOW_NAME_RISK_END)
public class RiskAnalysisEnd implements FlowNodeAction {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private IWorryFreeRiskItemService worryFreeRiskItemService;

    @Autowired
    private IRiskService riskService;

    @Override
    public void execute(FlowContext flowContext) {
        String userId = String.valueOf(flowContext.getVariable("id"));
        //首先往redis中写入状态--风险分析流程结束
        List<WorryFreeRiskItem> worryFreeRiskItems = new ArrayList<>();
        for (String index : WorryFreeConstant.getAllRiskTypeList()) {
            //风险项key
            String userZSetKey = "user_risk_scores:" + index + ":" + userId;
            //获取redis中的风险项 通过 zset分数进行排序
            RScoredSortedSet<String> riskTypeByZset = redissonClient.getScoredSortedSet(userZSetKey);
            Collection<String> riskNames = riskTypeByZset.valueRangeReversed(0, 5);
            if(CollectionUtil.isNotEmpty(riskNames)){
                //获取风险项名称
                LambdaQueryWrapper<Risk> riskQueryWrapper = new LambdaQueryWrapper<>();
                riskQueryWrapper.in(Risk::getRiskKey, riskNames);
                List<Risk> riskList = riskService.list(riskQueryWrapper);
                //构建风险项记录并添加到mysql中
                worryFreeRiskItems.add(WorryFreeRiskItem.builder().
                        customerId(Long.valueOf(userId)).
                        dataState(SuperConstant.DATA_STATE_0).
                        names(JSONUtil.parse(riskList.stream().map(Risk::getRiskName).collect(Collectors.toList())).toString()).
                        type(index).
                        build());
            }
        }
        worryFreeRiskItemService.saveBatch(worryFreeRiskItems);
        //获取风险项结果，存入mysql中
        //更新流程节点状态为风险分析流程结束
        //从redis中获取风险项结果，存入mysql中
        String endNode = WorryFreeConstant.FLOW_NAME_RISK_END;
        // redis队列的key
        String listKey = WorryFreeConstant.WORRYFREE_PREFIX + userId;
        // 往队列中添加数据
        redissonClient.getList(listKey).add(endNode);
    }

}