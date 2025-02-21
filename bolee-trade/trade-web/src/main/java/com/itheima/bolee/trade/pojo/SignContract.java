package com.itheima.bolee.trade.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName SignContract.java
 * @Description 签约合同
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_sign_contract")
@ApiModel(value="SignContract对象", description="签约合同")
public class SignContract extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SignContract(String agreementNo, Long id, String dataState, String contractNo, String externalAgreementNo, String signState, String tradeChannel, String rulePeriodType, Long rulePeriod, BigDecimal ruleTotalAmount, BigDecimal ruleSingleAmount, Long ruleTotalPayments) {
        super(id, dataState);
        this.contractNo = contractNo;
        this.externalAgreementNo = externalAgreementNo;
        this.signState = signState;
        this.tradeChannel = tradeChannel;
        this.rulePeriodType = rulePeriodType;
        this.rulePeriod = rulePeriod;
        this.ruleTotalAmount = ruleTotalAmount;
        this.ruleSingleAmount = ruleSingleAmount;
        this.ruleTotalPayments = ruleTotalPayments;
        this.agreementNo = agreementNo;
    }

    @ApiModelProperty(value = "合同编号:关联业务")
    private String contractNo;

    @ApiModelProperty(value = "商户签约号")
    private String externalAgreementNo;

    @ApiModelProperty(value = "支付宝签约号:关联支付")
    private String agreementNo;

    @ApiModelProperty(value = "签约状态：1. TEMP：暂存，协议未生效过；2. NORMAL：正常；3. STOP：暂停")
    private String signState;

    @ApiModelProperty(value = "支付渠道【支付宝、微信】")
    private String tradeChannel;

    @ApiModelProperty(value = "周期类型")
    private String rulePeriodType;

    @ApiModelProperty(value = "周期数")
    private Long rulePeriod;

    @ApiModelProperty(value = "总金额")
    private BigDecimal ruleTotalAmount;

    @ApiModelProperty(value = "总金额")
    private BigDecimal ruleSingleAmount;

    @ApiModelProperty(value = "扣款总次数")
    private Long ruleTotalPayments;
}
