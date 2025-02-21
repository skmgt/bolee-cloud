package com.itheima.bolee.trade.client.wechat.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName H5PayResponse.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
public class H5PayResponse extends BasicResponse{

    @JSONField(name="h5_url")
    private String h5Url;
}
