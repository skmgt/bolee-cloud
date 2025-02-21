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
 * @Description：退款记录表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_refund_record")
@ApiModel(value="RefundRecord对象", description="退款记录表")
public class RefundRecord extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public RefundRecord(Long id, String dataState, Long tradeOrderNo, Long productOrderNo, String refundNo,
                        String companyNo, String tradeChannel, String refundStatus, String refundCode,
                        String refundMsg, String memo, BigDecimal refundAmount) {
        super(id, dataState);
        this.tradeOrderNo = tradeOrderNo;
        this.productOrderNo = productOrderNo;
        this.refundNo = refundNo;
        this.companyNo = companyNo;
        this.tradeChannel = tradeChannel;
        this.refundStatus = refundStatus;
        this.refundCode = refundCode;
        this.refundMsg = refundMsg;
        this.memo = memo;
        this.refundAmount = refundAmount;
    }

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    private Long tradeOrderNo;

    @ApiModelProperty(value = "业务系统订单号")
    private Long productOrderNo;

    @ApiModelProperty(value = "本次退款订单号")
    private String refundNo;

    @ApiModelProperty(value = "商户号")
    private String companyNo;

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


}
