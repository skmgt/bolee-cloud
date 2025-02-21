package com.itheima.bolee.notify;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName NotificationRequest.java
 * @Description 通知请求对象
 */
@Data
@NoArgsConstructor
public class NotifyRequest {

    //时间戳
    private String timestamp;
    //随机字符串
    private String nonce;
    //签名
    private String signature;
    //请求体
    private String body;

    @Builder
    public NotifyRequest(String timestamp, String nonce, String signature, String body) {
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.signature = signature;
        this.body = body;
    }
}
