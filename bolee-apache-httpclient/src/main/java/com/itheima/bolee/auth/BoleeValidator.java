package com.itheima.bolee.auth;

import com.itheima.bolee.Validator;
import com.itheima.bolee.Verifier;
import com.itheima.bolee.notify.NotifyRequest;
import com.itheima.bolee.utils.ClientUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName BoleeValidator.java
 * @Description 签名验证处理实现
 */
public class BoleeValidator implements Validator {

    protected final Verifier verifier;

    public BoleeValidator(Verifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public boolean validate(CloseableHttpResponse response) throws IOException {
        //校验请求参数
        ClientUtils.validateParameters(response);
        //构建响应体信息
        String message = ClientUtils.responseMessage(response);
        //获得签名信息
        String signature = response.getFirstHeader("signature").getValue();
        //验证签名
        return verifier.verify(message.getBytes(StandardCharsets.UTF_8), Base64.decodeBase64(signature));
    }

    @Override
    public boolean validateNotify(NotifyRequest notifyRequest) {
        //校验请求参数
        ClientUtils.validateParametersNotify(notifyRequest);
        //构建响应体信息
        String message = ClientUtils.notifyMessage(notifyRequest);
        //获得签名信息
        String signature = notifyRequest.getSignature();
        //验证签名
        return verifier.verify(message.getBytes(StandardCharsets.UTF_8), Base64.decodeBase64(signature));
    }
}
