package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description：保险方案
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsurancePlan对象", description="保险方案")
public class InsurancePlanVO extends BaseVO {

    @Builder
    public InsurancePlanVO(Long planNo,BigDecimal maxPriceYear,BigDecimal maxPriceMonth,BigDecimal maxPriceWeek,BigDecimal maxPriceAllIn,Long id,String dataState,Long insuranceId,String palnName,String palnRemake,BigDecimal price,String priceUnit,Integer sortNo,Long[] insuranceIds){
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
        this.insuranceIds = insuranceIds;
        this.planNo=planNo;
    }

    @ApiModelProperty(value = "保险商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "保险方案保障项")
    private List<PlanSafeguardVO> planSafeguardVOs;

    @ApiModelProperty(value = "方案给付计划")
    private PlanEarningsVO planEarningsVO;

    @ApiModelProperty(value = "批量操作：保险id")
    private Long[] insuranceIds;
}
