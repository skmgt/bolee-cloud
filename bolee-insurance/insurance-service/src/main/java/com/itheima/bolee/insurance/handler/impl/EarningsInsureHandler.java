package com.itheima.bolee.insurance.handler.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyOrderConstant;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.insurance.constant.InsuranceConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.handler.InsureHandler;
import com.itheima.bolee.insurance.handler.InsureProcessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @ClassName EarningsInsureHandler.java
 * @Description 理财型：保险投保、创建订单、理财收益
 */
@Service("earningsInsureHandler")
public class EarningsInsureHandler implements InsureHandler {

    @Autowired
    InsureProcessHandler insureProcessHandler;

    @Autowired
    IdentifierGenerator identifierGenerator;

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
        //理财保险必填参数检测
        flag = insureProcessHandler.checkEarnings(coefficentVOs);
        if (!flag){
            throw new RuntimeException("理财型保险缺少必填参数!");
        }
        //理财投保金额是否符合检测
        flag = insureProcessHandler.checkPrice(insurancePlanVO,
                coefficentVOs,doInsureVo.getPrice());
        if (!flag){
            throw new RuntimeException("投保金额不符合!");
        }
        //检查投保年龄是否符合检测
        flag = insureProcessHandler.checkAge(insuranceVO,insured);
        if (!flag){
            throw new RuntimeException("投保年龄不符合!");
        }
        String warrantyNo= String.valueOf(identifierGenerator.nextId(insureProcessVO));
        //收益计算
        EarningVO earningVO = insureProcessHandler.earningsCompute(warrantyNo,applicant,doInsureVo,insurancePlanVO, coefficentVOs, insured,false);
        //犹豫期截止时间、等待期期截止时间、保障截止时间
        WarrantyTimeVO warrantyTimeVO = insureProcessHandler.timeSetup(doInsureVo,coefficentVOs, insuranceVO);
        //构建保障合同对象
        return WarrantyVO.builder()
            .insuranceId(insuranceVO.getId())
            .insuranceName(insuranceVO.getInsuranceName())
            .insuranceJson(JSONObject.toJSONString(insureProcessVO))
            .warrantyNo(warrantyNo)
            .applicantName(applicant.getName())
            .applicantIdentityCard(applicant.getIdentityCard())
            .companyNo(insureProcessVO.getCompanyVO().getCompanyNo())
            .companyName(insureProcessVO.getCompanyVO().getCompanName())
            .safeguardStartTime(warrantyTimeVO.getSafeguardStartTime())
            .safeguardEndTime(warrantyTimeVO.getSafeguardEndTime())
            .premium(doInsureVo.getPrice())
            .premiums(earningVO.getPremiums())
            .autoWarrantyExtension("0")
            .warrantyState(WarrantyConstant.STATE_NOT_PAY)
            .underwritingState(WarrantyConstant.UNDERWRITING_STATE_1)
            .periods(earningVO.getPeriods())
            .dutyPeriod(0)
            .hesitationTime(warrantyTimeVO.getHesitationTime())
            .waitTime(warrantyTimeVO.getWaitTime())
            .grace(insuranceVO.getGrace())
            .graceUnit(insuranceVO.getGraceUnit())
            .revival(insuranceVO.getRevival())
            .revivalUnit(insuranceVO.getRevivalUnit())
            .periodicUnit(earningVO.getPeriodicUnit())
            .customerRelationVOs(Lists.newArrayList(insured))
            .build();
    }

    @Override
    public List<WarrantyOrderVO> createWarrantyOrderVO(WarrantyVO warrantyVO) {
        List<WarrantyOrderVO> warrantyOrderVOs = Lists.newArrayList();
        //总周期数
        Integer periods = warrantyVO.getPeriods();
        for (int i = 1; i <= periods; i++) {
            WarrantyOrderVO warrantyOrderVO = WarrantyOrderVO.builder()
                .orderNo(String.valueOf(identifierGenerator.nextId(warrantyOrderVOs)))
                .warrantyNo(warrantyVO.getWarrantyNo())
                .companyNo(warrantyVO.getCompanyNo())
                .periods(warrantyVO.getPeriods())
                .currentPeriod(i)
                .premium(warrantyVO.getPremium())
                .premiums(warrantyVO.getPremiums())
                .periodicUnit(warrantyVO.getPeriodicUnit())
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
            if (periods!=1) {
                //计划执行时间
                LocalDateTime scheduleTime = null;
                if (i!=1) {
                    if (now.getDayOfMonth() <= 10) {
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 5, 0, 0);
                    } else if (now.getDayOfMonth() <= 20) {
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 15, 0, 0);
                    } else {
                        scheduleTime = LocalDateTime.of(now.getYear(), now.getMonth(), 25, 0, 0);
                    }
                    switch (warrantyVO.getPeriodicUnit()) {
                        //按周
                        case InsuranceConstant.WEEK:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i - 1, ChronoUnit.WEEKS);
                            break;
                        //按月
                        case InsuranceConstant.MONTH:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i - 1, ChronoUnit.MONTHS);
                            break;
                        //按年
                        case InsuranceConstant.YEAR:
                            scheduleTime = LocalDateTimeUtil.offset(scheduleTime, i - 1, ChronoUnit.YEARS);
                            break;
                        default:
                            throw new RuntimeException("周期单位不符合");
                    }
                }else {
                    scheduleTime = LocalDateTime.now();
                }
                warrantyOrderVO.setScheduleTime(scheduleTime);
            }else {
                warrantyOrderVO.setScheduleTime(now);
            }
            //宽限期截止时间
            if (!EmptyUtil.isNullOrEmpty(warrantyVO.getGraceUnit())) {
                LocalDateTime graceTime = null;
                switch (warrantyVO.getGraceUnit()) {
                    //按天
                    case InsuranceConstant.DAY:
                        graceTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getGrace(), ChronoUnit.DAYS);
                        break;
                    //按月
                    case InsuranceConstant.MONTH:
                        graceTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getGrace(), ChronoUnit.MONTHS);
                        break;
                    //按年
                    case InsuranceConstant.YEAR:
                        graceTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getGrace(), ChronoUnit.YEARS);
                        break;
                    default:
                        throw new RuntimeException("周期单位不符合");
                }
                warrantyOrderVO.setGraceTime(graceTime);
            }
            //复效期截止时间
            if (!EmptyUtil.isNullOrEmpty(warrantyVO.getRevivalUnit())) {
                LocalDateTime revivalTime = null;
                switch (warrantyVO.getRevivalUnit()) {
                    //按天
                    case InsuranceConstant.DAY:
                        revivalTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getRevival(), ChronoUnit.DAYS);
                        break;
                    //按月
                    case InsuranceConstant.MONTH:
                        revivalTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getRevival(), ChronoUnit.MONTHS);
                        break;
                    //按年
                    case InsuranceConstant.YEAR:
                        revivalTime = LocalDateTimeUtil.offset(warrantyOrderVO.getScheduleTime(), warrantyVO.getRevival(), ChronoUnit.YEARS);
                        break;
                    default:
                        throw new RuntimeException("周期单位不符合");
                }
                warrantyOrderVO.setRevivalTime(revivalTime);
            }
            warrantyOrderVO.setApplicantName(warrantyVO.getApplicantName());
            warrantyOrderVO.setApplicantIdentityCard(warrantyVO.getApplicantIdentityCard());
            warrantyOrderVOs.add(warrantyOrderVO);
        }
        return warrantyOrderVOs;
    }

    @Override
    public EarningVO doEarnings(DoInsureVo doInsureVo) {
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
        //投保人信息
        CustomerRelationVO applicant = insureProcessHandler.buildApplicant();
        //被投保人信息
        CustomerRelationVO insured = insureProcessHandler.buildInsured(doInsureVo.getCustomerRelationIds().get(0));
        //系数唯一性检查
        Boolean flag = insureProcessHandler.checkBaseOnly(coefficentVOs);
        if (!flag){
            throw new RuntimeException("相同系数多于2个!");
        }
        //理财保险必填参数检测
        flag = insureProcessHandler.checkEarnings(coefficentVOs);
        if (!flag){
            throw new RuntimeException("理财型保险缺少必填参数!");
        }
        //检查投保年龄是否符合检测
        flag = insureProcessHandler.checkAge(insuranceVO,insured);
        if (!flag){
            throw new RuntimeException("投保年龄不符合!");
        }
        //理财投保金额是否符合检测
        flag = insureProcessHandler.checkPrice(insurancePlanVO,
                coefficentVOs,doInsureVo.getPrice());
        if (!flag){
            throw new RuntimeException("投保金额不符合!");
        }
        //试算收益
        return insureProcessHandler.earningsCompute(null,applicant,doInsureVo,insurancePlanVO, coefficentVOs, insured,true);
    }

    @Override
    public String doPremium(DoInsureVo doTrialVo) {
        return null;
    }

}
