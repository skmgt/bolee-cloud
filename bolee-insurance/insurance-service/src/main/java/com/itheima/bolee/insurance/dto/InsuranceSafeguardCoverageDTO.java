package com.itheima.bolee.insurance.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * InsuranceCoverageDTO
 *
 * @author: wgl
 * @describe: 保障项详情类目
 * @date: 2022/12/28 10:10
 */
@Data
public class InsuranceSafeguardCoverageDTO {

    //保额
    private String val;

    //保险名称
    private String name;

    private BigDecimal score;
}
