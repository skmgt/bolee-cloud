package com.itheima.bolee.insurance.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.handler.InsureHandler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName TravelInsureHandler.java
 * @Description 旅游
 */
@Service("travelInsureHandler")
public class TravelInsureHandler extends SafeguardInsureHandler implements InsureHandler {

    @Override
    public WarrantyVO doInsure(DoInsureVo doInsureVo) {
        //投保对象信息
        InsureProcessVO insureProcessVO = insureProcessHandler.buildInsureProcessVO(doInsureVo.getInsuranceId(),
            doInsureVo.getInsurancePlanId(),
            doInsureVo.getCompanyNo(),
            doInsureVo.getInsuranceCoefficentIds());
        //保险产品
        InsuranceVO insuranceVO = insureProcessVO.getInsuranceVO();
        //保险方案
        InsurancePlanVO insurancePlanVO = insureProcessVO.getInsurancePlanVO();
        //保险系数
        List<InsuranceCoefficentVO> coefficentVOs = insureProcessVO.getCoefficents();
        //投保人信息
        CustomerRelationVO applicant = insureProcessHandler.buildApplicant();
        //系数唯一性检查
        Boolean flag = insureProcessHandler.checkBaseOnly(coefficentVOs);
        if (!flag){
            throw new RuntimeException("相同系数多于2个!");
        }
        //旅游型保险必填参数检测
        flag = insureProcessHandler.checkTravel(coefficentVOs);
        if (!flag){
            throw new RuntimeException("旅游型保险缺少必填参数!");
        }
        //多位被投保人信息
        List<CustomerRelationVO> customerRelationVOs = insureProcessHandler.buildInsureds(doInsureVo.getCustomerRelationIds());
        //投保人数系数
        coefficentVOs = insureProcessHandler.numberOfPeopleHandler(coefficentVOs,insuranceVO.getId(),customerRelationVOs);
        //保费计算
        String price = insureProcessHandler.premiumComputeTravel(insurancePlanVO, coefficentVOs);
        doInsureVo.setPrice(new BigDecimal(price));
        //总周期计算
        Integer periods = 1;
        //犹豫期截止时间、等待期期截止时间、保障截止时间
        WarrantyTimeVO warrantyTimeVO = insureProcessHandler.timeSetup(doInsureVo,coefficentVOs, insuranceVO);
        //是否自动续保
        String autoWarrantyExtension = "0";
        //周期单位
        String periodicUnit = "";
        //构建保障合同对象
        return WarrantyVO.builder()
            .insuranceId(insuranceVO.getId())
            .insuranceName(insuranceVO.getInsuranceName())
            .insuranceJson(JSONObject.toJSONString(insureProcessVO))
            .warrantyNo(String.valueOf(identifierGenerator.nextId(insureProcessVO)))
            .applicantName(applicant.getName())
            .applicantIdentityCard(applicant.getIdentityCard())
            .companyNo(insureProcessVO.getCompanyVO().getCompanyNo())
            .companyName(insureProcessVO.getCompanyVO().getCompanName())
            .safeguardStartTime(warrantyTimeVO.getSafeguardStartTime())
            .safeguardEndTime(warrantyTimeVO.getSafeguardEndTime())
            .premium(doInsureVo.getPrice())
            .premiums(doInsureVo.getPrice())
            .autoWarrantyExtension(autoWarrantyExtension)
            .warrantyState(WarrantyConstant.STATE_NOT_PAY)
            .underwritingState(WarrantyConstant.UNDERWRITING_STATE_1)
            .periods(periods)
            .dutyPeriod(0)
            .hesitationTime(warrantyTimeVO.getHesitationTime())
            .waitTime(warrantyTimeVO.getWaitTime())
            .grace(insuranceVO.getGrace())
            .graceUnit(insuranceVO.getGraceUnit())
            .revival(insuranceVO.getRevival())
            .revivalUnit(insuranceVO.getRevivalUnit())
            .periodicUnit(periodicUnit)
            .customerRelationVOs(customerRelationVOs)
            .build();
    }

    @Override
    public String doPremium(DoInsureVo doInsureVo) {
        //投保对象信息
        InsureProcessVO insureProcessVO = insureProcessHandler.buildInsureProcessVO(doInsureVo.getInsuranceId(),
            doInsureVo.getInsurancePlanId(),
            doInsureVo.getCompanyNo(),
            doInsureVo.getInsuranceCoefficentIds());
        //保险产品
        InsuranceVO insuranceVO = insureProcessVO.getInsuranceVO();
        //保险方案
        InsurancePlanVO insurancePlanVO = insureProcessVO.getInsurancePlanVO();
        //保险系数
        List<InsuranceCoefficentVO> coefficentVOs = insureProcessVO.getCoefficents();
        //多位被投保人信息
        List<CustomerRelationVO> customerRelationVOs = insureProcessHandler.buildInsureds(doInsureVo.getCustomerRelationIds());
        //投保人信息
        CustomerRelationVO applicant = insureProcessHandler.buildApplicant();
        //系数唯一性检查
        Boolean flag = insureProcessHandler.checkBaseOnly(coefficentVOs);
        if (!flag){
            throw new RuntimeException("相同系数多于2个!");
        }
        //团险型保险必填参数检测
        flag = insureProcessHandler.checkTravel(coefficentVOs);
        if (!flag){
            throw new RuntimeException("旅游型保险缺少必填参数!");
        }
        //投保人数系数
        coefficentVOs = insureProcessHandler.numberOfPeopleHandler(coefficentVOs,insuranceVO.getId(),customerRelationVOs);
        //保费计算
        return insureProcessHandler.premiumComputeTravel(insurancePlanVO, coefficentVOs);
    }
}
