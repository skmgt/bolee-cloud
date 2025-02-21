package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：保险方案
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_plan")
@ApiModel(value="InsurancePlan对象", description="保险方案")
public class InsurancePlan extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsurancePlan(Long planNo,BigDecimal maxPriceYear,BigDecimal maxPriceMonth,BigDecimal maxPriceWeek,BigDecimal maxPriceAllIn,Long id,String dataState,Long insuranceId,String palnName,String palnRemake,BigDecimal price,String priceUnit,Integer sortNo){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.palnName=palnName;
        this.palnRemake=palnRemake;
        this.price=price;
        this.priceUnit=priceUnit;
        this.sortNo=sortNo;
        this.maxPriceYear =maxPriceYear;
        this.maxPriceMonth =maxPriceMonth;
        this.maxPriceWeek =maxPriceWeek;
        this.maxPriceAllIn=maxPriceAllIn;
        this.planNo=planNo;
    }

    @ApiModelProperty(value = "保险商品id")
    private Long insuranceId;

    @ApiModelProperty(value = "保险方案名称")
    private String palnName;

    @ApiModelProperty(value = "保险方案编号")
    private Long planNo;

    @ApiModelProperty(value = "保险方案名称补充")
    private String palnRemake;

    @ApiModelProperty(value = "默认定价")
    private BigDecimal price;

    @ApiModelProperty(value = "默认定价单位：y/d,y/m,y/y")
    private String priceUnit;

    @ApiModelProperty(value = "每年最高")
    private BigDecimal maxPriceYear;

    @ApiModelProperty(value = "每月最高")
    private BigDecimal maxPriceMonth;

    @ApiModelProperty(value = "每周最高")
    private BigDecimal maxPriceWeek;

    @ApiModelProperty(value = "一次性投入最高")
    private BigDecimal maxPriceAllIn;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
