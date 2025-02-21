package com.itheima.bolee.framework.commons.dto.trade;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName AliAppPeriodic.java
 * @Description 阿里APP周期扣款参数对象
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AliPeriodicVO implements Serializable {

    @Builder
    public AliPeriodicVO(String agreementNo,String contractNo, String externalAgreementNo, String signScene, String signNotifyUrl, String rulePeriodType, String rulePeriod, String ruleExecuteTime, String ruleTotalAmount, String ruleSingleAmount, String ruleTotalPayments, String accessChannel) {
        this.contractNo = contractNo;
        this.externalAgreementNo = externalAgreementNo;
        this.agreementNo = agreementNo;
        this.signScene = signScene;
        this.signNotifyUrl = signNotifyUrl;
        this.rulePeriodType = rulePeriodType;
        this.rulePeriod = rulePeriod;
        this.ruleExecuteTime = ruleExecuteTime;
        this.ruleTotalAmount = ruleTotalAmount;
        this.ruleSingleAmount = ruleSingleAmount;
        this.ruleTotalPayments = ruleTotalPayments;
        this.accessChannel = accessChannel;
    }

    @ApiModelProperty(value = "签约扣款：合同号")
    private String contractNo;

    @ApiModelProperty(value = "签约扣款：商户签约号")
    private String externalAgreementNo;

    @ApiModelProperty(value = "支付宝签约号:关联支付")
    private String agreementNo;

    @ApiModelProperty(value = "签约扣款：扣款场景")
    private String signScene;

    @ApiModelProperty(value = "签约扣款：签约成功异步通知地址")
    private String signNotifyUrl;

    @ApiModelProperty(value = "签约扣款：周期类型")
    private String rulePeriodType;

    @ApiModelProperty(value = "签约扣款：周期数")
    private String rulePeriod;

    @ApiModelProperty(value = "签约扣款：下次扣款的时间")
    private String ruleExecuteTime;

    @ApiModelProperty(value = "签约扣款：扣款总金额")
    private String ruleTotalAmount;

    @ApiModelProperty(value = "签约扣款：单次扣款最大金额")
    private String ruleSingleAmount;

    @ApiModelProperty(value = "签约扣款：总扣款次数")
    private String ruleTotalPayments;

    @ApiModelProperty(value = "签约扣款：通道")
    private String accessChannel;
}
