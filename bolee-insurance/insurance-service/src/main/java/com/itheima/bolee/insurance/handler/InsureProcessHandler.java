package com.itheima.bolee.insurance.handler;

import com.itheima.bolee.insurance.dto.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName InsureProcessHandler.java
 * @Description 投保业务操作流程
 */
public interface InsureProcessHandler {


    /***
     * @description 投保对象信息
     * @param insuranceId 保险产品Id
     * @param insurancePlanId 保险方案Id
     * @param companyNo 保险公司编号
     * @param InsuranceCoefficentIds 投保系数Id
     * @return
     */
    InsureProcessVO buildInsureProcessVO(Long insuranceId, Long insurancePlanId,
                                         String companyNo, List<Long> InsuranceCoefficentIds);

    /***
     * @description 个险：被投保人信息
     * @param customerRelationId 客户关系ID
     * @return
     */
    CustomerRelationVO buildInsured(String customerRelationId);

    /***
     * @description 团险：被投保人信息
     * @param customerRelationIds 客户关系IDs
     * @return
     */
    List<CustomerRelationVO> buildInsureds(List<String> customerRelationIds);

    /***
     * @description 投保人信息
     * @return
     */
    CustomerRelationVO buildApplicant();

    /**
     * @description 系数唯一性检查
     * @param insuranceCoefficentVOs 保险系数
     * @return: Boolean
     */
    Boolean checkBaseOnly(List<InsuranceCoefficentVO> insuranceCoefficentVOs);

    /**
     * @description 个险：投保年龄是否符合检测
     * @param insuranceVO 保险产品
     * @param insured 被投保人
     * @return: Boolean
     */
    Boolean checkAge(InsuranceVO insuranceVO,CustomerRelationVO insured);

    /**
     * @description 理财：投保金额是否符合检测
     * @param insurancePlanVO 保险方案
     * @param insuranceCoefficentVOs 保险系数
     * @param price 投入金额
     * @return: Boolean
     */
    Boolean checkPrice(InsurancePlanVO insurancePlanVO,List<InsuranceCoefficentVO> insuranceCoefficentVOs, BigDecimal price);


    /**
     * @description 保障：保险必填参数检测
     * @param coefficentVOs 保险系数
     * @return: Boolean
     */
    Boolean checkSafeguard(List<InsuranceCoefficentVO> coefficentVOs);

    /**
     * @description 旅游：保险必填参数检测
     * @param coefficentVOs 保险系数
     * @return: Boolean
     */
    Boolean checkTravel(List<InsuranceCoefficentVO> coefficentVOs);

    /**
     * @description 理财：保险必填参数检测
     * @param coefficentVOs 保险系数
     * @return: Boolean
     */
    Boolean checkEarnings(List<InsuranceCoefficentVO> coefficentVOs);

    /**
     * @description 个险：被保人年龄归属投保系数
     * @param coefficentVOs 页面提交保险系数
     * @param insuranceId 保险产品
     * @param insured 被投保人
     * @return: Boolean
     */
    List<InsuranceCoefficentVO> ageHandler(List<InsuranceCoefficentVO> coefficentVOs,Long insuranceId,CustomerRelationVO insured);

    /**
     * @description 团险：被保人数量归属投保系数
     * @param coefficentVOs 页面提交保险系数
     * @param insuranceId 保险产品
     * @param insureds 被投保人
     * @return:  List<InsuranceCoefficentVO>
     */
    List<InsuranceCoefficentVO> numberOfPeopleHandler(List<InsuranceCoefficentVO> coefficentVOs,Long insuranceId, List<CustomerRelationVO> insureds);

    /***
     * @description 旅游：保费计算
     * @param insurancePlanVO 所选保险方案
     * @param coefficentVOs 所选保险系数
     * @return  保费
     */
    String premiumComputeTravel(InsurancePlanVO insurancePlanVO, List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 保障：保费计算
     * @param insurancePlanVO 所选保险方案
     * @param coefficentVOs 所选保险系数
     * @return  保费
     */
    String premiumComputeSafeguard(InsurancePlanVO insurancePlanVO,List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 理财：收益计算
     * @param warrantyNo 合同编号
     * @param insurancePlanVO 所选保险方案
     * @param coefficentVOs 所选保险系数
     * @param insured 被投保人
     * @param isTrial 是否试算
     * @return  保费
     */
    EarningVO earningsCompute(String warrantyNo,CustomerRelationVO applicant,DoInsureVo doInsureVo,
                              InsurancePlanVO insurancePlanVO,
                              List<InsuranceCoefficentVO> coefficentVOs,
                              CustomerRelationVO insured,Boolean isTrial);

    /***
     * @description 领取计划
     * @param warrantyNo 合同编号
     * @param applicant 投保人
     * @param insured 被投保人
     * @param insurancePlanVO 保险方案
     * @param coefficentVOs 系数
     * @param premiums 总保费
     * @param actualGetPeriodicUnit 领取单位
     * @param actualGetStartTime 领取开始时间
     * @return 总周期
     */
    List<PeriodicVo> periodicVos(String warrantyNo, CustomerRelationVO applicant, CustomerRelationVO insured,InsurancePlanVO insurancePlanVO, BigDecimal premiums,
                                  List<InsuranceCoefficentVO> coefficentVOs, String actualGetPeriodicUnit,
                                  LocalDateTime actualGetStartTime,Boolean isTrial,DoInsureVo doInsureVo);

    /***
     * @description 理财：总周期计算
     * @param coefficentVOs 保险系数
     * @return 总周期
     */
    Integer earningsPeriods(List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 保障：总周期计算
     * @param coefficentVOs 保险系数
     * @return 总周期
     */
    Integer SafeguardPeriods(List<InsuranceCoefficentVO> coefficentVOs);


    /***
     * @description 犹豫期截止时间、等待期期截止时间、保障截止时间
     * @return
     */
    WarrantyTimeVO timeSetup(DoInsureVo doInsureVo,List<InsuranceCoefficentVO> coefficentVOs, InsuranceVO insuranceVO);

    /***
     * @description 是否自动续保
     * @param coefficentVOs 保险系数
     * @return
     */
    String autoWarrantyExtension(List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 投入周期时长
     * @param coefficentVOs 保险系数
     * @return 周期时长
     */
    BigDecimal periodic(List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 理财：投入周期单位
     * @param coefficentVOs 保险系数
     * @return
     */
    String periodicUnit(List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 保障：付款方式
     * @param coefficentVOs 保险系数
     * @return
     */
    String payMent(List<InsuranceCoefficentVO> coefficentVOs);

    /***
     * @description 领取起始时间
     * @param coefficentVOs 保险系数
     * @param insured 被投保人
     * @return
     */
    LocalDateTime actualGetStartTime(List<InsuranceCoefficentVO> coefficentVOs, CustomerRelationVO insured );

    /***
     * @description 理财影响系数:领取，投入方式不同会影响最最终收益
     * @param coefficentVOs 保险系数
     * @param premiums 原始保费
     * @return
     */
    BigDecimal premiumsHandler(List<InsuranceCoefficentVO> coefficentVOs,BigDecimal premiums);

    /***
     * @description 领取单位
     * @param coefficentVOs 保险系数
     * @return
     */
    String actualGetPeriodicUnit(List<InsuranceCoefficentVO> coefficentVOs);
}
