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
import java.time.LocalDateTime;
import java.util.Date;
/**
 * @Description：给付计划订单
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WarrantyEarningsOrder对象", description="给付计划订单")
public class WarrantyEarningsOrderVO extends BaseVO {

    @Builder
    public WarrantyEarningsOrderVO(Long id,String dataState,String orderNo,BigDecimal premium,String warrantyNo,String currentPeriod,LocalDateTime scheduleTime,LocalDateTime actualTime,String orderState,String applicantName,String applicantIdentityCard,Integer sortNo){
        super(id, dataState);
        this.orderNo=orderNo;
        this.premium=premium;
        this.warrantyNo=warrantyNo;
        this.currentPeriod=currentPeriod;
        this.scheduleTime=scheduleTime;
        this.actualTime=actualTime;
        this.orderState=orderState;
        this.applicantName=applicantName;
        this.applicantIdentityCard=applicantIdentityCard;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "当期赔付")
    private BigDecimal premium;

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "当前期数")
    private String currentPeriod;

    @ApiModelProperty(value = "计划时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduleTime;

    @ApiModelProperty(value = "实际时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualTime;

    @ApiModelProperty(value = "状态（0未赔付 1已赔付 2 断保中止 3断保终止）")
    private String orderState;

    @ApiModelProperty(value = "投保人姓名")
    private String applicantName;

    @ApiModelProperty(value = "投保人身份证号码")
    private String applicantIdentityCard;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
