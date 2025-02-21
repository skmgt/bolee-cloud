package com.itheima.bolee.insurance.handler.impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.dto.DoInsureVo;
import com.itheima.bolee.insurance.dto.InsureProcessVO;
import com.itheima.bolee.insurance.dto.WarrantyVO;
import com.itheima.bolee.insurance.handler.InsureHandler;
import com.itheima.bolee.insurance.handler.InsureProcessHandler;
import com.itheima.bolee.insurance.rule.AccessRuleDTO;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;
import com.itheima.bolee.insurance.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MedicalInsureHandler.java
 * @Description 保障型医疗类:保险投保、创建订单、保费计算
 */
@Service
public class MedicalInsureHandler extends SafeguardInsureHandler implements InsureHandler {

    @Autowired
    InsureProcessHandler insureProcessHandler;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    IRuleService ruleService;

    @Override
    public WarrantyVO doInsure(DoInsureVo doInsureVo) {
        //问卷：是否有问题处理
        if (doInsureVo.getIsProblem().equals(InsureConstant.IS_PROBLEM_0)){
            //医疗和重疾需要跑规则引擎
            AdviceHealthDTO adviceHealthDTO = ruleService.submitQuestionChoose(doInsureVo.getAdviceHealthDTO());
            if (adviceHealthDTO.getResult()){
                return super.doInsure(doInsureVo);
            }else {
                throw new RuntimeException("初筛不通过");
            }
        }else {
            return super.doInsure(doInsureVo);
        }
    }
}
