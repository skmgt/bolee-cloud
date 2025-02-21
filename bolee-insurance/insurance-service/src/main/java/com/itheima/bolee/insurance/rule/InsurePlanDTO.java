package com.itheima.bolee.insurance.rule;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

import java.math.BigDecimal;

/**
 * InsurePlanDTO
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Data
public class InsurePlanDTO {

    @Label(value = "保险商品id")
    private Long insuranceId;

    @Label(value = "保险方案名称")
    private String palnName;

    @Label(value = "保险方案名称补充")
    private String palnRemake;

    @Label(value = "默认定价")
    private BigDecimal price;

    @Label(value = "默认定价单位：y/d,y/m,y/y")
    private String priceUnit;

    @Label(value = "高额利率")
    private BigDecimal rateTopGrade;

    @Label(value = "上年利率")
    private BigDecimal rateLastYear;

    @Label(value = "保障利率")
    private BigDecimal rateGuarantee;

    @Label(value = "排序")
    private Integer sortNo;

}