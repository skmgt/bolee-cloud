package com.itheima.bolee.framework.commons.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TradeVO.java
 * @Description 交易结果
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TradeVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public TradeVO(Long id,String notifyUrl, String dataState, String openId, Long productOrderNo, Long tradeOrderNo, String tradeChannel,
                   String tradeState, String payeeName, Long payeeId, String payerName, Long payerId, BigDecimal tradeAmount,
                   BigDecimal refund, String isRefund, String resultCode, String resultMsg, String resultJson, String placeOrderCode,
                   String placeOrderMsg, String placeOrderJson, String memo, String qrCodeImageBase64, String outRequestNo,
                   BigDecimal operTionRefund, String authCode, String quitUrl, String returnUrl, String billType, Date billDate,
                   String billDownloadUrl, String companyNo) {
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
        this.memo = memo;
        this.qrCodeImageBase64 = qrCodeImageBase64;
        this.outRequestNo = outRequestNo;
        this.operTionRefund = operTionRefund;
        this.authCode = authCode;
        this.quitUrl = quitUrl;
        this.returnUrl = returnUrl;
        this.notifyUrl=notifyUrl;
        this.billType = billType;
        this.billDate = billDate;
        this.billDownloadUrl = billDownloadUrl;
        this.companyNo = companyNo;

    }

    @ApiModelProperty(value = "openId标识")
    private String openId;

    @ApiModelProperty(value = "业务系统订单号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productOrderNo;

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tradeOrderNo;

    @ApiModelProperty(value = "支付渠道【支付宝、微信】")
    private String tradeChannel;

    @ApiModelProperty(value = "交易单状态0待付款 1已支付 2已关闭）")
    private String tradeState;

    @ApiModelProperty(value = "收款人姓名")
    private String payeeName;

    @ApiModelProperty(value = "收款人账户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long payeeId;

    @ApiModelProperty(value = "付款人姓名")
    private String payerName;

    @ApiModelProperty(value = "付款人Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long payerId;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "退款总金额")
    private BigDecimal refund;

    @ApiModelProperty(value = "是否有退款：0，1")
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

    @ApiModelProperty(value = "备注【订单门店，桌台信息】")
    private String memo;

    @ApiModelProperty(value = "二维码base64")
    private String qrCodeImageBase64;

    @ApiModelProperty(value = "退款请求号")
    private String outRequestNo;

    @ApiModelProperty(value = "本次退款金额")
    private BigDecimal operTionRefund;

    @ApiModelProperty(value = "支付授权码")
    private String authCode;

    @ApiModelProperty(value = "支付宝：HTTP/HTTPS开头字符串")
    private String quitUrl;

    @ApiModelProperty(value = "支付宝：用户付款中途退出返回商户网站的地址")
    private String returnUrl;

    @ApiModelProperty(value = "支付宝：异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型")
    private String billType;

    @ApiModelProperty(value = "账单时间:日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单。不支持下载当日账单，只能下载前一日24点前的账单数据（T+1）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")//get
    protected Date billDate;

    @ApiModelProperty(value = "账单地址")
    private String billDownloadUrl;

    @ApiModelProperty(value = "商户ID【系统内部识别使用】")
    private String companyNo;

    @ApiModelProperty(value = "退款记录")
    private List<RefundRecordVO> refundRecordVOList;

    @ApiModelProperty(value = "阿里APP周期扣款签约参数对象")
    private AliPeriodicVO aliPeriodicVO;

}
