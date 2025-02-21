package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.WarrantyApplicantVO;
import com.itheima.bolee.insurance.pojo.WarrantyInsured;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WarrantyApplicantService.java
 * @Description 合同客户相关
 */
public interface IWarrantyCustomerService {

    /***
     * @description 按保单号查询投保人信息
     * @param warrantyNo
     * @return
     */
    WarrantyApplicantVO findWarrantyApplicant(String warrantyNo);

    /***
     * @description 按保单号查询被投保人信息
     * @param warrantyNo
     * @return
     */
    List<WarrantyInsured> findWarrantyInsured(String warrantyNo);
}
