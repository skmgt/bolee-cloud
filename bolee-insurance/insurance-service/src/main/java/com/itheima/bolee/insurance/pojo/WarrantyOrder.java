package com.itheima.bolee.insurance.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description：合同订单
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_warranty_order")
@ApiModel(value="WarrantyOrder对象", description="合同订单")
public class WarrantyOrder extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WarrantyOrder(String agreementNo,String applicantName,String applicantIdentityCard,Long id, String dataState, String orderNo, String warrantyNo, Integer periods, Integer currentPeriod, BigDecimal premium, BigDecimal premiums, String periodicUnit, LocalDateTime scheduleTime, LocalDateTime actualTime, LocalDateTime graceTime, LocalDateTime revivalTime, String orderState, Integer sortNo, String companyNo, String tradingChannel, String agentId, String agentName, BigDecimal agentFee, BigDecimal platformFee) {
        super(id, dataState);
        this.orderNo = orderNo;
        this.warrantyNo = warrantyNo;
        this.periods = periods;
        this.currentPeriod = currentPeriod;
        this.premium = premium;
        this.premiums = premiums;
        this.periodicUnit = periodicUnit;
        this.scheduleTime = scheduleTime;
        this.actualTime = actualTime;
        this.graceTime = graceTime;
        this.revivalTime = revivalTime;
        this.orderState = orderState;
        this.sortNo = sortNo;
        this.companyNo = companyNo;
        this.tradingChannel = tradingChannel;
        this.agreementNo = agreementNo;
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentFee = agentFee;
        this.platformFee = platformFee;
        this.applicantName = applicantName;
        this.applicantIdentityCard = applicantIdentityCard;
    }

    @ApiModelProperty(value = "投保人姓名")
    private String applicantName;

    @ApiModelProperty(value = "投保人身份证号码")
    private String applicantIdentityCard;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "总期数")
    private Integer periods;

    @ApiModelProperty(value = "当前期数")
    private Integer currentPeriod;

    @ApiModelProperty(value = "期交保费")
    private BigDecimal premium;

    @ApiModelProperty(value = "总保费")
    private BigDecimal premiums;

    @ApiModelProperty(value = "周期单位")
    private String periodicUnit;

    @ApiModelProperty(value = "支付宝签约号:关联支付")
    private String agreementNo;

    @ApiModelProperty(value = "计划时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime scheduleTime;

    @ApiModelProperty(value = "实际时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime actualTime;

    @ApiModelProperty(value = "宽限期截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime graceTime;

    @ApiModelProperty(value = "复效期截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime revivalTime;

    @ApiModelProperty(value = "状态（0待付款 1已付款 2逾期 3补缴 4付款失败 5付款中）")
    private String orderState;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "支付渠道【支付宝、微信、三方银行】")
    private String tradingChannel;

    @ApiModelProperty(value = "代理人Id")
    private String agentId;

    @ApiModelProperty(value = "代理人姓名")
    private String agentName;

    @ApiModelProperty(value = "代理人费用")
    private BigDecimal agentFee;

    @ApiModelProperty(value = "平台代理费用")
    private BigDecimal platformFee;
}
