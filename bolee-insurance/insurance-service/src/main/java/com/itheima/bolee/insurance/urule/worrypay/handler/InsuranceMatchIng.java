package com.itheima.bolee.insurance.urule.worrypay.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.ProductTypeDTO;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContext;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.pojo.WorryFreeInsuranceMatch;
import com.itheima.bolee.insurance.service.ICategorySafeguardService;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.IWorryFreeInsuranceMatchService;
import com.itheima.bolee.insurance.urule.anno.FlowName;
import com.itheima.bolee.insurance.urule.worrypay.FlowNodeAction;
import com.itheima.bolee.wenxin.WenXinService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * InsuranceMatchStart
 *
 * @author: wgl
 * @describe: 产品匹配逻辑处理节点
 * @date: 2022/12/28 10:10
 */
@FlowName(WorryFreeConstant.FLOW_NAME_PRODUCT_MATCH_ING)
@Slf4j
public class InsuranceMatchIng implements FlowNodeAction {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private IInsuranceService insuranceService;

    @Autowired
    private WenXinService wenXinService;

    @Autowired
    private ICategorySafeguardService categorySafeguardService;

    @Autowired
    private IWorryFreeInsuranceMatchService worryFreeInsuranceMatchService;


    //进行产品匹配
    @Override
    public void execute(FlowContext flowContext) {
        String userId = String.valueOf(flowContext.getVariable("id"));
        //查询出系统中所有的保险数据
        InsuranceVO insuranceVO = InsuranceVO.builder().
                dataState(InsureConstant.DATA_STATE_0).
                build();
        //需要为当前的用户匹配保险产品
        List<InsuranceVO> insuranceVOS = insuranceService.findList(insuranceVO);

        List<CategorySafeguardVO> safeguardByMedical = categorySafeguardService.findList(CategorySafeguardVO.builder().dataState(SuperConstant.DATA_STATE_0).build());
        Map<String, List<CategorySafeguardVO>> safeguardTyepMap = safeguardByMedical.stream().collect(Collectors.groupingBy(e -> e.getCategoryNo()));

        Map<Long, List<InsuranceVO>> categoryByInsurance = insuranceVOS.stream().collect(Collectors.groupingBy(e -> e.getCategoryNo()));

        //获取配额节点分配的保障项和对应的医疗，意外，身故，重疾的配额
        List<String> safeguardList = (List<String>) flowContext.getVariable(WorryFreeConstant.VARIABLE_KEY_SAFEGUARD);

        List<WorryFreeInsuranceMatch> res = new ArrayList<WorryFreeInsuranceMatch>();
        //拿到对应分类的保险
        for (ProductTypeDTO category : WorryFreeConstant.getProductTypeCategoryList()) {
            //构建当前产品类目的保险列表--未来需要从这个列表中找到最合适的保险
            List<InsuranceVO> categoryTypeByInsurance = new ArrayList<>();
            List<String> categoryNos = new ArrayList<>();
            for (Long indexId : categoryByInsurance.keySet()) {
                //判断是否是医疗分类
                if (NoProcessing.isParent(category.getCategoryNo(), String.valueOf(indexId))) {
                    //匹配医疗保险
                    categoryTypeByInsurance.addAll(categoryByInsurance.get(indexId));
                    categoryNos.add(String.valueOf(indexId));
                }
            }
            //找到分类对应分类下的保障项
            List<CategorySafeguardVO> categorySafeguardVOS = new ArrayList<>();
            for (String indexCategory : categoryNos) {
                categorySafeguardVOS.addAll(safeguardTyepMap.get(indexCategory));
            }

            //在对应分类中找到规则引擎匹配的保障项的键
            List<String> medicalKeys = categorySafeguardVOS.stream().
                    filter(e -> safeguardList.contains(e.getSafeguardKey())).
                    map(e -> e.getSafeguardKey()).
                    collect(Collectors.toList());

            BigDecimal amount = (BigDecimal) flowContext.getVariable(category.getCategoryKey());
            //根据保额，保障项，找到分类下最合适的保险及其方案
            InsurancePlanTimeDTO bestInsurancePlan = getBestInsurance(categoryTypeByInsurance, medicalKeys, amount, category.getCategoryKey());
            //暂时不添加gpt -- 仅记录保险计划方案
            if(ObjectUtil.isNotNull(bestInsurancePlan)){
                res.add(WorryFreeInsuranceMatch.builder().
                        dataState(SuperConstant.DATA_STATE_0).
                        amount(bestInsurancePlan.getAmount().longValue()).
                        type(bestInsurancePlan.getType()).
                        insuranceJson(JSONUtil.parseObj(bestInsurancePlan.getInsuranceVO()).toString()).
                        insurancePlan(JSONUtil.parseObj(bestInsurancePlan.getInsurancePlanVO()).toString()).
                        customerId(Long.valueOf(userId)).
                        price(bestInsurancePlan.getInsurancePlanVO().getPrice()).
                        insuranceId(String.valueOf(bestInsurancePlan.getInsuranceVO().getId())).
                        build());
            }
        }
        //数据入库
        if(CollectionUtil.isNotEmpty(res)){
            worryFreeInsuranceMatchService.saveBatch(res);
        }
    }

    /**
     * 获取最匹配的保险
     *
     * @param insurances  当前分类下的保险
     * @param medicalKeys 当前分类下匹配的保障项
     * @param amount    期望的保额
     * @param categoryKey 分类 意外，重疾，医疗
     * @return
     */
    private InsurancePlanTimeDTO getBestInsurance(List<InsuranceVO> insurances, List<String> medicalKeys, BigDecimal amount, String categoryKey) {
        //开始构建保障项map  key为保障项出现次数，value为对应的保险方案
        List<InsurancePlanTimeDTO> timesInsurancePlans = new ArrayList<InsurancePlanTimeDTO>();
        for (InsuranceVO index : insurances) {
            List<InsurancePlanVO> insurancePlanVOs = index.getInsurancePlanVOs();
            for (InsurancePlanVO planVO : insurancePlanVOs) {
                int count = 0;
                BigDecimal safeguardTotalAmount = BigDecimal.ZERO;
                List<String> safeGuardVOS = planVO.getPlanSafeguardVOs().stream().map(PlanSafeguardVO::getSafeguardKey).collect(Collectors.toList());
                for (String indexSafeGuard : safeGuardVOS) {
                    if (medicalKeys.contains(indexSafeGuard)) {
                        count++;
                    }
                }
                List<String> safeguard = planVO.getPlanSafeguardVOs().stream().map(PlanSafeguardVO::getSafeguardValue).collect(Collectors.toList());
                for (String indexSafeGuard : safeguard) {
                    //{"val": "1000", "name": "1000元", "score": "1000"}
                    String val = JSONObject.parseObject(indexSafeGuard, JsonScoreAttribute.class).getVal();
                    if (val.contains("-")) {
                        String[] split = val.split("-");
                        val = split[0];
                    }
                    //求和所有的保障项的保障金额
                    try {
                        long valNum = Long.parseLong(val);
                        safeguardTotalAmount = safeguardTotalAmount.add(BigDecimal.valueOf(valNum));
                    }catch (Exception e){
                        //由于存在一些时间格式的保障项，暂时忽略
                        continue;
                    }
                }
                InsurancePlanTimeDTO insurancePlanTimeDTO = new InsurancePlanTimeDTO();
                insurancePlanTimeDTO.setTimes(count);
                insurancePlanTimeDTO.setInsuranceVO(index);
                insurancePlanTimeDTO.setInsurancePlanVO(planVO);
                insurancePlanTimeDTO.setAmount(safeguardTotalAmount);
                insurancePlanTimeDTO.setType(categoryKey);
                timesInsurancePlans.add(insurancePlanTimeDTO);
            }
        }
        //按照统计次数排序
        timesInsurancePlans.sort(Comparator.comparingInt(e -> e.getTimes()));
        InsurancePlanTimeDTO bestMatch = findBestMatch(timesInsurancePlans, amount);
        return bestMatch;
    }

    /**
     * 获取最匹配的保险方案
     *
     * @param plans
     * @param targetAmount
     * @return
     */
    public InsurancePlanTimeDTO findBestMatch(List<InsurancePlanTimeDTO> plans, BigDecimal targetAmount) {
        //找到最匹配的保险方案
        InsurancePlanTimeDTO bestMatch = null;
        //最小差距
        BigDecimal closestAmountDifference = BigDecimal.ZERO;
        //最高次数
        Integer highestTimes = BigDecimal.ZERO.intValue();

        //遍历所有保险方案，找到最匹配的方案
        for (InsurancePlanTimeDTO plan : plans) {
            //获取当前保险方案的总保额
            BigDecimal currentAmount = plan.getAmount();
            //期望保额和当前保额的差额
            BigDecimal amountDifference = currentAmount.subtract(targetAmount).abs();
            //找到当前方案和期望保额差距最小的方案
            if (plan.getTimes() >= highestTimes ||
                    (plan.getTimes() == highestTimes && amountDifference.compareTo(closestAmountDifference) < 0)) {
                highestTimes = plan.getTimes();
                closestAmountDifference = amountDifference;
                bestMatch = plan;
            }
        }
        return bestMatch;
    }
}
