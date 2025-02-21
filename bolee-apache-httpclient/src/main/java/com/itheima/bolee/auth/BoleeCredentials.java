package com.itheima.bolee.auth;

import com.itheima.bolee.Credentials;
import com.itheima.bolee.Signer;
import com.itheima.bolee.utils.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestWrapper;

import java.io.IOException;

/**
 * @ClassName BoleeCredentials.java
 * @Description 认证凭证创建处理实现
 */
@Slf4j
public class BoleeCredentials implements Credentials {

    //应用id
    protected final String appId;

    //签名者
    protected final Signer signer;

    public BoleeCredentials(String appId, Signer signer) {
        this.appId = appId;
        this.signer = signer;
    }

    @Override
    public String createCredentials(HttpRequestWrapper request) throws IOException {
        //临时字符串
        String nonceStr = ClientUtils.generateNonceStr();
        //时间戳
        long timestamp = ClientUtils.generateTimestamp();
        //请求数据
        String message = ClientUtils.requestMessage(nonceStr, timestamp, request);
        log.debug("authorization data:{}", message);
        //生成签名字符串
        String signature = signer.sign(Base64.decodeBase64(message));
        //返回认证令牌对象
        String credentials = "appId=\"" + this.appId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "signature=\"" + signature + "\"";
        log.debug("authorization credentials=[{}]", credentials);
        return credentials;
    }
}