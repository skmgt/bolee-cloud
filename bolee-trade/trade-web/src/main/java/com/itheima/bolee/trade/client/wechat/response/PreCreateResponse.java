package com.itheima.bolee.trade.client.wechat.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PreCreateResponse.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
public class PreCreateResponse extends BasicResponse{

    //二维码请求地址
    @JSONField(name="code_url")
    private String codeUrl;

}
