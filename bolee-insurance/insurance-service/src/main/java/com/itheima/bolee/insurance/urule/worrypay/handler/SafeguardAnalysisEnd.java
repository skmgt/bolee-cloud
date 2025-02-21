package com.itheima.bolee.insurance.urule.worrypay.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.insurance.dto.WorryFreeSafeguardQuotaVO;
import com.itheima.bolee.insurance.pojo.Risk;
import com.itheima.bolee.insurance.pojo.WorryFreeRiskItem;
import com.itheima.bolee.insurance.service.IRiskService;
import com.itheima.bolee.insurance.service.IWorryFreeRiskItemService;
import com.itheima.bolee.insurance.service.IWorryFreeSafeguardQuotaService;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SafeguardAnalysisEnd
 *
 * @author: wgl
 * @describe: 评估保障结束
 * @date: 2022/12/28 10:10
 */
@FlowName(WorryFreeConstant.FLOW_NAME_SAFEGUARD_ANALYSIS_END)
public class SafeguardAnalysisEnd implements FlowNodeAction {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private IRiskService riskService;

    @Autowired
    private IWorryFreeSafeguardQuotaService worryFreeSafeguardQuotaService;



    @Override
    public void execute(FlowContext flowContext) {
        //=============================配额记录入库===============================
        String userId = String.valueOf(flowContext.getVariable("id"));
        //首先往redis中写入状态--风险分析流程结束
        List<WorryFreeRiskItem> worryFreeRiskItems = new ArrayList<>();
        List<String> safeguardList =(List<String>) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_SAFEGUARD);
        BigDecimal medicalAmount = (BigDecimal) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_MEDICAL_AMOUNT);
        BigDecimal accidentAmount = (BigDecimal) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_ACCIDENT_AMOUNT);
        BigDecimal dieAmount = (BigDecimal) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_DIE_AMOUNT);
        BigDecimal seriousAmount = (BigDecimal) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_SERIOUS_AMOUNT);
        WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO = WorryFreeSafeguardQuotaVO.builder().
                safeguards(JSONUtil.parse(safeguardList).toString()).
                customerId(Long.valueOf(userId)).
                medicalAmount(medicalAmount.longValue()).
                accidentAmount(accidentAmount.longValue()).
                dieAmount(dieAmount.longValue()).
                seriousAmount(seriousAmount.longValue()).
                dataState(SuperConstant.DATA_STATE_0).
                build();
        worryFreeSafeguardQuotaService.save(worryFreeSafeguardQuotaVO);
        //=============================修改进度条状态===============================
        //从redis中获取风险项结果，存入mysql中
        String endNode = WorryFreeConstant.FLOW_NAME_SAFEGUARD_ANALYSIS_END;
        // redis队列的key
        String listKey = WorryFreeConstant.WORRYFREE_PREFIX + userId;
        // 往队列中添加数据
        redissonClient.getList(listKey).add(endNode);
    }
}