package com.itheima.bolee;

import org.apache.http.client.methods.HttpRequestWrapper;

import java.io.IOException;

/**
 * @ClassName Credentials.java
 * @Description 认证凭证创建处理
 */
public interface Credentials {

    /***
     * @description 创建请求认证凭证
     * @param request 请求对象
     * @return: java.lang.String 凭证字符串
     */
    String createCredentials(HttpRequestWrapper request) throws IOException;
}
