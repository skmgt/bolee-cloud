package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.enums.WarrantyApplicantEnum;
import com.itheima.bolee.insurance.pojo.WarrantyInsured;
import com.itheima.bolee.insurance.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WarrantyCustomerServiceImpl.java
 * @Description 合同客户相关
 */
@Service("warrantyCustomerService")
@Slf4j
public class WarrantyCustomerServiceImpl implements IWarrantyCustomerService {

    @Autowired
    IWarrantyService warrantyService;

    @Autowired
    IWarrantyInsuredService warrantyInsuredService;

    @Autowired
    ICustomerCardService customerCardService;

    @Autowired
    ICustomerInfoService customerInfoService;

    /**
     * 获取投保人信息
     *
     * @param warrantyNo
     * @return
     */
    @Override
    public WarrantyApplicantVO findWarrantyApplicant(String warrantyNo) {
        try {
            WarrantyVO warranty = warrantyService.findByWarrantyNo(warrantyNo);
            CustomerInfoVO customerInfo = customerInfoService.findOneByIdentityCard(warranty.getApplicantIdentityCard());
            CustomerCardVO customerCardVO = CustomerCardVO.builder().customerId(customerInfo.getCustomerId()).dataState(SuperConstant.DATA_STATE_0).build();
            List<CustomerCardVO> customerCardList = customerCardService.findList(customerCardVO);
            WarrantyApplicantVO res = WarrantyApplicantVO.buildApplicantInfo(customerInfo, customerCardList, warranty.getApplicantName());
            //查询合同
            return res;
        } catch (Exception e) {
            log.error("投保人信息构建错误：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyApplicantEnum.APPLICANT_ERROR);
        }
    }

    /**
     * 获取被保人信息
     *
     * @param warrantyNo
     * @return
     */
    @Override
    public List<WarrantyInsured> findWarrantyInsured(String warrantyNo) {
        try {
            WarrantyInsuredVO warrantyInsuredVO = WarrantyInsuredVO.builder().warrantyNo(warrantyNo).dataState(SuperConstant.DATA_STATE_0).build();
            List<WarrantyInsuredVO> warrantyInsuredVOS = warrantyInsuredService.findList(warrantyInsuredVO);
            //获取合同投保信息后
            List<WarrantyInsured> warrantyInsureds = BeanConv.toBeanList(warrantyInsuredVOS, WarrantyInsured.class);

            List<WarrantyInsured> res = new ArrayList<>();
            //属性替换
            for (WarrantyInsured index : warrantyInsureds) {
                WarrantyInsured warrantyInsured = new WarrantyInsured(index.getWarrantyNo(),index.getInsuredName(),index.getInsuredIdentityCard(),index.getCompanyNo());
                res.add(warrantyInsured);
            }
            return res;
        } catch (Exception e) {
            log.error("被投保人信息构建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyApplicantEnum.INSURED_ERROR);
        }
    }
}
