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
 * @Description：交易订单表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_trade")
@ApiModel(value="Trade对象", description="交易订单表")
public class Trade extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Trade(Long id, String dataState, String openId, Long productOrderNo, Long tradeOrderNo, String tradeChannel, String tradeState, String payeeName, Long payeeId, String payerName, Long payerId, BigDecimal tradeAmount, BigDecimal refund, String isRefund, String resultCode, String resultMsg, String resultJson, String placeOrderCode, String placeOrderMsg, String placeOrderJson, String companyNo, String memo) {
        super(id, dataState);
        this.openId = openId;
        this.productOrderNo = productOrderNo;
        this.tradeOrderNo = tradeOrderNo;
        this.tradeChannel = tradeChannel;
        this.tradeState = tradeState;
        this.payeeName = payeeName;
        this.payeeId = payeeId;
        this.payerName = payerName;
        this.payerId = payerId;
        this.tradeAmount = tradeAmount;
        this.refund = refund;
        this.isRefund = isRefund;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultJson = resultJson;
        this.placeOrderCode = placeOrderCode;
        this.placeOrderMsg = placeOrderMsg;
        this.placeOrderJson = placeOrderJson;
        this.companyNo = companyNo;
        this.memo = memo;
    }

    @ApiModelProperty(value = "openId标识")
    private String openId;

    @ApiModelProperty(value = "业务系统订单号")
    private Long productOrderNo;

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    private Long tradeOrderNo;

    @ApiModelProperty(value = "支付渠道【支付宝、微信】")
    private String tradeChannel;

    @ApiModelProperty(value = "交易单状态 0待付款 1已支付 2已关闭）")
    private String tradeState;

    @ApiModelProperty(value = "收款人姓名")
    private String payeeName;

    @ApiModelProperty(value = "收款人账户ID")
    private Long payeeId;

    @ApiModelProperty(value = "付款人姓名")
    private String payerName;

    @ApiModelProperty(value = "付款人Id")
    private Long payerId;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "退款金额【付款后】")
    private BigDecimal refund;

    @ApiModelProperty(value = "是否有退款：YES，NO")
    private String isRefund;

    @ApiModelProperty(value = "第三方交易返回编码【最终确认交易结果】")
    private String resultCode;

    @ApiModelProperty(value = "第三方交易返回提示消息【最终确认交易信息】")
    private String resultMsg;

    @ApiModelProperty(value = "第三方交易返回信息json【分析交易最终信息】")
    private String resultJson;

    @ApiModelProperty(value = "统一下单返回编码")
    private String placeOrderCode;

    @ApiModelProperty(value = "统一下单返回信息")
    private String placeOrderMsg;

    @ApiModelProperty(value = "统一下单返回信息json【用于生产二维码、Android ios唤醒支付等】")
    private String placeOrderJson;

    @ApiModelProperty(value = "商户ID【系统内部识别使用】")
    private String companyNo;

    @ApiModelProperty(value = "备注【订单门店，桌台信息】")
    private String memo;


}
