package com.itheima.bolee;

import com.itheima.bolee.notify.NotifyRequest;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * @ClassName Validator.java
 * @Description 签名验证处理
 */
public interface Validator {

    /***
     * @description 验证应答签名
     * @param response 响应结果
     * @return: boolean 校验结果
     */
    boolean validate(CloseableHttpResponse response) throws IOException;

    /***
     * @description 验证通知请求签名
     * @param notifyRequest 响应结果
     * @return: boolean 校验结果
     */
    boolean validateNotify(NotifyRequest notifyRequest);
}
