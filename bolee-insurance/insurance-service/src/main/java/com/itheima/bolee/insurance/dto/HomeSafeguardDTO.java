package com.itheima.bolee.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HomeSafeguardDTO
 *
 * @author: wgl
 * @describe: 家庭保障DTO对象
 * @date: 2022/12/28 10:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeSafeguardDTO {

    /**
     * 保险ID
     */
    private String insuranceId;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 合同编号
     */
    private String warrantyNo;

    /**
     * 保单类型
     */
    private String checkRule;
}
