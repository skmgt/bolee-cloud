package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.dto.DoInsureVo;
import com.itheima.bolee.insurance.dto.InsureProcessVO;
import com.itheima.bolee.insurance.dto.WorryFreeCustomerInfoVO;
import com.itheima.bolee.insurance.rule.AccessRuleDTO;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;

/**
 * IRuleService
 *
 * @author: wgl
 * @describe: 规则引擎service
 * @date: 2022/12/28 10:10
 */
public interface IRuleService {


    /**
     * 省心配规则
     * @param customerInfoVO
     */
    WorryFreeCustomerInfoVO worryFreePairing(WorryFreeCustomerInfoVO customerInfoVO);


    void testRule(String id);

    void testRuleTable(String id);

    /**
     * 保险初筛
     * @param adviceHealthDTO
     * @return
     */
    AdviceHealthDTO submitQuestionChoose(AdviceHealthDTO adviceHealthDTO);

    /**
     * 设置基本属性
     * @param worryFreeCustomerInfoVO
     */
    void buildBaseParams(WorryFreeCustomerInfoVO worryFreeCustomerInfoVO);
}
