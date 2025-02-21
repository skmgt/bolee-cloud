package com.itheima.bolee.framework.commons.enums.trade;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName TradeEnum.java
 * @Description TODO
 */
public enum TradeEnum implements IBaseEnum {

    CONFIG_ERROR(43002, "微信支付配置错误！"),
    CHECK_TRADE_FAIL(43003,"交易单校验失败"),
    PAYING_TRADE_FAIL(43004, "交易单支付失败"),
    TRADE_STATE_SUCCEED(43005, "交易单已完成,不可重复支付"),
    TRADE_STATE_PAYING(43006, "交易单交易中,不可重复支付"),
    TRYLOCK_TRADE_FAIL(43007, "交易加锁失败"),
    CONFIG_EMPT(43008, "支付配置为空"),
    TRAD_PAY_FAIL(43009, "统一下单交易失败"),
    TRAD_DEDUCTION_FAIL(43009, "周期性扣款失败"),
    TRAD_SIGN_CONTRACT_FAIL(43009, "周期性扣款签约失败"),
    TRAD_QRCODE_FAIL(43010, "生成二维码失败"),
    TRAD_QUERY_FAIL(43011, "查询统一下单交易失败"),
    TRAD_REFUND_FAIL(43012, "统一下单退款交易失败"),
    TRAD_QUERY_REFUND_FAIL(43013, "统一下单退款交易查询失败"),
    TRAD_CLOSE_FAIL(43014, "统一下单退款交易失败");

    private Integer code;
    private String msg;

    TradeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
