package com.itheima.bolee.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * InsurancePlanTimeDTO
 *
 * @author: wgl
 * @describe: 保险方案次数DTO
 * @date: 2022/12/28 10:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlanTimeDTO {

    /**
     * 包含保障项次数
     */
    private Integer times;

    /**
     * 保险
     */
    private InsuranceVO insuranceVO;

    /**
     * 对应的保险方案
     */
    private InsurancePlanVO insurancePlanVO;

    /**
     * 方案对应的保额
     */
    private BigDecimal amount;


    /**
     * 对应的类型 医疗 意外 重疾
     */
    private String type;


    /**
     * 保险建议
     */
    private String insuranceSuggest;

    /**
     * 解决方案
     */
    private String solution;
}
