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
 * @Description：保险合同
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_warranty")
@ApiModel(value="Warranty对象", description="保险合同")
public class Warranty extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Warranty(Long id, String dataState, Long insuranceId, String insuranceName, String insuranceJson, String warrantyNo, String applicantName, String applicantIdentityCard, String companyNo, String companyName, LocalDateTime safeguardStartTime, LocalDateTime safeguardEndTime, BigDecimal premium, BigDecimal premiums, String autoWarrantyExtension, String warrantyState, String underwritingState, Integer periods, Integer dutyPeriod, Integer sortNo, LocalDateTime hesitationTime, LocalDateTime waitTime, Integer grace, String graceUnit, Integer revival, String revivalUnit, String periodicUnit, String agentId, String agentName) {
        super(id, dataState);
        this.insuranceId = insuranceId;
        this.insuranceName = insuranceName;
        this.insuranceJson = insuranceJson;
        this.warrantyNo = warrantyNo;
        this.applicantName = applicantName;
        this.applicantIdentityCard = applicantIdentityCard;
        this.companyNo = companyNo;
        this.companyName = companyName;
        this.safeguardStartTime = safeguardStartTime;
        this.safeguardEndTime = safeguardEndTime;
        this.premium = premium;
        this.premiums = premiums;
        this.autoWarrantyExtension = autoWarrantyExtension;
        this.warrantyState = warrantyState;
        this.underwritingState = underwritingState;
        this.periods = periods;
        this.dutyPeriod = dutyPeriod;
        this.sortNo = sortNo;
        this.hesitationTime = hesitationTime;
        this.waitTime = waitTime;
        this.grace = grace;
        this.graceUnit = graceUnit;
        this.revival = revival;
        this.revivalUnit = revivalUnit;
        this.periodicUnit = periodicUnit;
        this.agentId = agentId;
        this.agentName = agentName;
    }

    @ApiModelProperty(value = "保险ID")
    private Long insuranceId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "投保详情")
    private String insuranceJson;

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "投保人姓名")
    private String applicantName;

    @ApiModelProperty(value = "投保人身份证号码")
    private String applicantIdentityCard;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "保障起始时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime safeguardStartTime;

    @ApiModelProperty(value = "保障截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime safeguardEndTime;

    @ApiModelProperty(value = "保费")
    private BigDecimal premium;

    @ApiModelProperty(value = "总保费")
    private BigDecimal premiums;

    @ApiModelProperty(value = "自动延保（0是 1否）")
    private String autoWarrantyExtension;

    @ApiModelProperty(value = "状态（0待付款 1待生效 2保障中  3 逾期中止 4理赔终止 5 复效中止 6 复效终止  6满期终止 7 拒保 8 犹豫期退保 9 协议退保   ）")
    private String warrantyState;

    @ApiModelProperty(value = "核保状态(0发送失败 1核保中 2核保失败 3核保成功  )")
    private String underwritingState;

    @ApiModelProperty(value = "批保状态(0未批保 1批保发送失败 2批保中 3批保通过 4.保批不通过)")
    private String approveState;

    @ApiModelProperty(value = "总期数")
    private Integer periods;

    @ApiModelProperty(value = "已缴纳期数")
    private Integer dutyPeriod;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "犹豫期截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime hesitationTime;

    @ApiModelProperty(value = "等待期截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime waitTime;

    @ApiModelProperty(value = "保单宽限")
    private Integer grace;

    @ApiModelProperty(value = "宽限单位")
    private String graceUnit;

    @ApiModelProperty(value = "保单复效")
    private Integer revival;

    @ApiModelProperty(value = "复效单位")
    private String revivalUnit;

    @ApiModelProperty(value = "周期单位")
    private String periodicUnit;

    @ApiModelProperty(value = "代理人Id")
    private String agentId;

    @ApiModelProperty(value = "代理人姓名")
    private String agentName;
}
