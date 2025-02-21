package com.itheima.bolee.trade.client.wechat.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AmountResponse.java
 * @Description 金额信息
 */
@Data
@NoArgsConstructor
public class AmountResponse {

    //订单总金额【分】
    private Integer total;

    //退款总金额【分】
    private Integer refund;
}
