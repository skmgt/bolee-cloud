package com.itheima.bolee.framework.commons.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RefundRecordVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public RefundRecordVO(Long id, String dataState, Long tradeOrderNo, Long productOrderNo, String refundNo, String tradeChannel, String refundStatus, String refundCode, String refundMsg, String memo, BigDecimal refundAmount, String companyNo) {
        super(id, dataState);
        this.tradeOrderNo = tradeOrderNo;
        this.productOrderNo = productOrderNo;
        this.refundNo = refundNo;
        this.tradeChannel = tradeChannel;
        this.refundStatus = refundStatus;
        this.refundCode = refundCode;
        this.refundMsg = refundMsg;
        this.memo = memo;
        this.refundAmount = refundAmount;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tradeOrderNo;

    @ApiModelProperty(value = "业务系统订单号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productOrderNo;

    @ApiModelProperty(value = "本次退款订单号")
    private String refundNo;

    @ApiModelProperty(value = "退款渠道【支付宝、微信、现金】")
    private String tradeChannel;

    @ApiModelProperty(value = "退款状态【成功：SUCCESS,进行中：SENDING】")
    private String refundStatus;

    @ApiModelProperty(value = "返回编码")
    private String refundCode;

    @ApiModelProperty(value = "返回信息")
    private String refundMsg;

    @ApiModelProperty(value = "备注【订单门店，桌台信息】")
    private String memo;

    @ApiModelProperty(value = "本次退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "商户号")
    private String companyNo;
}
