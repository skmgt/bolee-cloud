package com.itheima.bolee;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpStatus.SC_MULTIPLE_CHOICES;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * @ClassName SignatureExec.java
 * @Description 请求执行者
 */
@Slf4j
public class DecorateClientExecChain implements ClientExecChain {

    private final ClientExecChain mainExec;

    private final Credentials credentials;

    private final Validator validator;

    protected DecorateClientExecChain(Credentials credentials, Validator validator, ClientExecChain mainExec) {
        this.credentials = credentials;
        this.validator = validator;
        this.mainExec = mainExec;
    }

    protected void convertToRepeatableResponseEntity(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            response.setEntity(new BufferedHttpEntity(entity));
        }
    }

    @Override
    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper,
                                         HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware) throws IOException, HttpException {
        //请求头添加认证令牌
        httpRequestWrapper.addHeader(AUTHORIZATION,credentials.createCredentials(httpRequestWrapper));
        //执行请求
        CloseableHttpResponse response = mainExec.execute(httpRoute, httpRequestWrapper, httpClientContext, httpExecutionAware);
        //对成功应答验签
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= SC_OK && statusLine.getStatusCode() < SC_MULTIPLE_CHOICES) {
            convertToRepeatableResponseEntity(response);
            if (!validator.validate(response)) {
                throw new HttpException("应答的签名验证失败");
            }
        }
        return response;
    }

}
