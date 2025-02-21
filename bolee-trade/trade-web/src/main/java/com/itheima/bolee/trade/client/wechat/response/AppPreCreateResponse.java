package com.itheima.bolee.trade.client.wechat.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AppPreCreateResponse.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
public class AppPreCreateResponse {

    //请求返回编码
    private String code;

    @JSONField(name="prepay_id")
    private String prepayId;
}
