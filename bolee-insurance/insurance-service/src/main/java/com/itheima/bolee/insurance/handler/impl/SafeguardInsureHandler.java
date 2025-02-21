package com.itheima.bolee.insurance.handler.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyOrderConstant;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.insurance.constant.InsuranceConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.handler.InsureHandler;
import com.itheima.bolee.insurance.handler.InsureProcessHandler;
import com.itheima.bolee.insurance.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @ClassName SafeguardInsureHandler.java
 * @Description 保障型：保险投保、创建订单、保费计算
 */
@Service("safeguardInsureHandler")
public class SafeguardInsureHandler implements InsureHandler {

    @Autowired
    InsureProcessHandler insureProcessHandler;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    IRuleService ruleService;

    @Override
    public WarrantyVO doInsure(DoInsureVo doInsureVo) {
        //不支持团险
        if (doInsureVo.getCustomerRelationIds().size()>1){
            throw new RuntimeException("不支持团险");
        }
        //不支持指定生效期
        if (!EmptyUtil.isNullOrEmpty(doInsureVo.getSafeguardStartTime())){
            throw new RuntimeException("不支持指定生效期");
        }
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
        //被投保人关系ID
        String customerRelationId = doInsureVo.getCustomerRelationIds().get(0);
        //被投保人信息
        CustomerRelationVO insured = insureProcessHandler.buildInsured(customerRelationId);
        //投保人信息
        CustomerRelationVO applicant = insureProcessHandler.buildApplicant();
        //系数唯一性检查
        Boolean flag = insureProcessHandler.checkBaseOnly(coefficentVOs);
        if (!flag){
           throw new RuntimeException("相同系数多于2个!");
        }
        //保障型保险必填参数检测
        flag = insureProcessHandler.checkSafeguard(coefficentVOs);
        if (!flag){
            throw new RuntimeException("保障型保险缺少必填参数!");
        }
        //投保年龄是否符合检测
        flag = insureProcessHandler.checkAge(insuranceVO,insured);
        if (!flag){
            throw new RuntimeException("投保年龄不符合!");
        }
        //投保年龄系数
        coefficentVOs = insureProcessHandler.ageHandler(coefficentVOs,insuranceVO.getId(),insured);
        //保费计算
        doInsureVo.setPrice(new BigDecimal(insureProcessHandler.premiumComputeSafeguard(insurancePlanVO, coefficentVOs)));
        //总周期计算
        Integer periods = insureProcessHandler.SafeguardPeriods(coefficentVOs);
        //周期单位
        String periodicUnit = insureProcessHandler.payMent(coefficentVOs);
        //犹豫期截止时间、等待期截止时间、保障截止时间
        WarrantyTimeVO warrantyTimeVO = insureProcessHandler.timeSetup(doInsureVo,coefficentVOs, insuranceVO);
        //是否自动续保
        String autoWarrantyExtension = insureProcessHandler.autoWarrantyExtension(coefficentVOs);
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
            .premiums(doInsureVo.getPrice().multiply(new BigDecimal(periods)))
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
            .customerRelationVOs(Lists.newArrayList(insured))
            .build();
    }

    @Override
    public List<WarrantyOrderVO> createWarrantyOrderVO(WarrantyVO warrantyVO) {
        List<WarrantyOrderVO> warrantyOrderVOs = Lists.newArrayList();
        //总周期数
        Integer periods = warrantyVO.getPeriods();
        for (int i = 1; i <= periods; i++) {
            //构建WarrantyOrderVO
            WarrantyOrderVO warrantyOrderVO = WarrantyOrderVO.builder()
                .orderNo(String.valueOf(identifierGenerator.nextId(warrantyOrderVOs)))
                .warrantyNo(warrantyVO.getWarrantyNo())
                .companyNo(warrantyVO.getCompanyNo())
                .periods(warrantyVO.getPeriods())
                .currentPeriod(i)
                .premium(warrantyVO.getPremium())
                .periodicUnit(warrantyVO.getPeriodicUnit())
                .premiums(warrantyVO.getPremiums())
                .orderState(WarrantyOrderConstant.ORDER_STATE_0)
                .build();
            //利润分成
            if (i==1){
                InsureProcessVO insureProcessVO = JSONObject.parseObject(warrantyVO.getInsuranceJson(), InsureProcessVO.class);
                //有代理人
                if (!EmptyUtil.isNullOrEmpty(warrantyVO.getAgentId())){
                    BigDecimal individualAgentRate = insureProcessVO.getInsuranceVO().getIndividualAgentRate();
                    warrantyOrderVO.setAgentFee(warrantyVO.getPremium().multiply(individualAgentRate).divide(new BigDecimal(100)));
                    BigDecimal platformAgentRate = insureProcessVO.getInsuranceVO().getPlatformAgentRate();
                    warrantyOrderVO.setPlatformFee(warrantyVO.getPremium().multiply(platformAgentRate).divide(new BigDecimal(100)));
                //无代理人
                }else {
                    warrantyOrderVO.setAgentFee(BigDecimal.ZERO);
                    BigDecimal individualAgentRate = insureProcessVO.getInsuranceVO().getIndividualAgentRate();
                    BigDecimal platformAgentRate = insureProcessVO.getInsuranceVO().getPlatformAgentRate().add(individualAgentRate);
                    warrantyOrderVO.setPlatformFee(warrantyVO.getPremium().multiply(platformAgentRate).divide(new BigDecimal(100)));
                }
            }else {
                warrantyOrderVO.setAgentFee(BigDecimal.ZERO);
                warrantyOrderVO.setPlatformFee(BigDecimal.ZERO);
            }
            LocalDateTime now = LocalDateTime.now();
            if (periods!=1){
                //计划执行时间
                LocalDateTime scheduleTime = null;
                if (i!=1){
                    if (now.getDayOfMonth()<=10){
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 5, 0, 0);
                    }else if(now.getDayOfMonth()<=20){
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 15, 0, 0);
                    }else {
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 25, 0, 0);
                    }
                    switch (warrantyVO.getPeriodicUnit()){
                        //按周
                        case InsuranceConstant.WEEK:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i-1, ChronoUnit.WEEKS);
                            break;
                        //按月
                        case InsuranceConstant.MONTH:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i-1, ChronoUnit.MONTHS);
                            break;
                        //按年
                        case InsuranceConstant.YEAR:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i-1, ChronoUnit.YEARS);
                            break;
                        default:
                            throw new RuntimeException("周期单位不符合");
                    }
                }else {
                    scheduleTime = LocalDateTime.now();
                }
                warrantyOrderVO.setScheduleTime(scheduleTime);
                //宽限期截止时间
                if (!EmptyUtil.isNullOrEmpty(warrantyVO.getGraceUnit())){
                    LocalDateTime graceTime = null ;
                    switch (warrantyVO.getGraceUnit()){
                        //按周
                        case InsuranceConstant.DAY:
                            graceTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getGrace(), ChronoUnit.WEEKS);
                            break;
                        //按月
                        case InsuranceConstant.MONTH:
                            graceTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getGrace(), ChronoUnit.MONTHS);
                            break;
                        //按年
                        case InsuranceConstant.YEAR:
                            graceTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getGrace(), ChronoUnit.YEARS);
                            break;
                        default:
                            throw new RuntimeException("周期单位不符合");
                    }
                    warrantyOrderVO.setGraceTime(graceTime);
                }
                //复效期截止时间
                if (!EmptyUtil.isNullOrEmpty(warrantyVO.getRevivalUnit())){
                    LocalDateTime revivalTime = null;
                    switch (warrantyVO.getRevivalUnit()){
                        //按周
                        case InsuranceConstant.DAY:
                            revivalTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getRevival(), ChronoUnit.WEEKS);
                            break;
                        //按月
                        case InsuranceConstant.MONTH:
                            revivalTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getRevival(), ChronoUnit.MONTHS);
                            break;
                        //按年
                        case InsuranceConstant.YEAR:
                            revivalTime = LocalDateTimeUtil.offset(scheduleTime, warrantyVO.getRevival(), ChronoUnit.YEARS);
                            break;
                        default:
                            throw new RuntimeException("周期单位不符合");
                    }
                    warrantyOrderVO.setRevivalTime(revivalTime);
                }
            }else {
                warrantyOrderVO.setScheduleTime(now);
            }
            warrantyOrderVO.setApplicantName(warrantyVO.getApplicantName());
            warrantyOrderVO.setApplicantIdentityCard(warrantyVO.getApplicantIdentityCard());
            warrantyOrderVOs.add(warrantyOrderVO);
        }
        return warrantyOrderVOs;
    }

    @Override
    public String doPremium(DoInsureVo doInsureVo) {
        //判断个险类
        if (doInsureVo.getCustomerRelationIds().size()>1){
            throw new RuntimeException("不支持团险");
        }
        //个险类：不支持指定生效期
        if (!EmptyUtil.isNullOrEmpty(doInsureVo.getSafeguardStartTime())){
            throw new RuntimeException("不支持指定生效期");
        }
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
        //被投保人信息
        CustomerRelationVO insured = insureProcessHandler.buildInsured(doInsureVo.getCustomerRelationIds().get(0));
        //检测投保年龄
        Boolean flag = insureProcessHandler.checkAge(insuranceVO,insured);
        if (!flag){
            return "-1";
        }
        //检查系数唯一性
        flag = insureProcessHandler.checkBaseOnly(coefficentVOs);
        if (!flag){
            throw new RuntimeException("相同系数多于2个!");
        }
        //检测个险型必填系数
        flag = insureProcessHandler.checkSafeguard(coefficentVOs);
        if (!flag){
            throw new RuntimeException("保障型保险缺少必填参数!");
        }
        //投保年龄系数
        coefficentVOs = insureProcessHandler.ageHandler(coefficentVOs,insuranceVO.getId(),insured);
        //保费计算
        return insureProcessHandler.premiumComputeSafeguard(insurancePlanVO,coefficentVOs);
    }

    @Override
    public EarningVO doEarnings(DoInsureVo doInsureVo) {
        return null;
    }

}
