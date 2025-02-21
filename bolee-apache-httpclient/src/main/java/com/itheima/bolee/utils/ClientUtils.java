package com.itheima.bolee.utils;

import com.itheima.bolee.notify.NotifyRequest;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

/**
 * @ClassName ClientUtils.java
 * @Description 客户端工具类
 */
public class ClientUtils {

    //随机字符串处理
    protected static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //随机函数
    protected static final SecureRandom RANDOM = new SecureRandom();

    /***
     * @description 时间戳创建
     * @return: long 返回时间戳
     */
    public static long generateTimestamp() {

        return System.currentTimeMillis() / 1000;
    }

    /***
     * @description 随机字符串
     * @return: java.lang.String 字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[16];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /***
     * @description 构建请求信息: 请求方式+请求路径+请求时间戳+随机字符串+请求体
     * @param nonce 随机字符串
     * @param timestamp 时间戳
     * @param request 请求对象
     * @return: java.lang.String 请求信息
     */
    public static String requestMessage(String nonce, long timestamp, HttpRequestWrapper request) throws IOException {
        URI uri = request.getURI();
        String canonicalUrl = uri.getRawPath();
        if (uri.getQuery() != null) {
            canonicalUrl += "?" + uri.getRawQuery();
        }
        String body = EntityUtils.toString(((HttpEntityEnclosingRequest) request).getEntity(), StandardCharsets.UTF_8);
        return request.getRequestLine().getMethod() + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonce + "\n"
                + body + "\n";
    }

    /***
     * @description 检测应答参数
     * @param response 应答对象
     */
    public static final void validateParameters(CloseableHttpResponse response) {
        //返回头中存在的内容：签名字符串，随机字符串，时间戳
        String[] headers = {"signature","nonce_str","timestamp"};
        Header header = null;
        for (String headerName : headers) {
            header = response.getFirstHeader(headerName);
            if (header == null) {
                throw new RuntimeException("验证返回参数"+headerName+"不全");
            }
        }
        //获得最后时间戳，判断应答时间戳到当前时间超过5分钟则认为应答失败
        String timestampStr = header.getValue();
        try {
            Instant responseTime = Instant.ofEpochSecond(Long.parseLong(timestampStr));
            // 拒绝过期应答
            if (Duration.between(responseTime, Instant.now()).abs().toMinutes() >= 5) {
                throw new RuntimeException("应答超时");
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw new RuntimeException("应答时间处理异常");
        }
    }

    /***
     * @description 构建应答信息
     * @param response 应答结果
     * @return: java.lang.String 应答信息
     */
    public static String responseMessage(CloseableHttpResponse response) throws IOException {
        String timestamp = response.getFirstHeader("timestamp").getValue();
        String nonce = response.getFirstHeader("nonce_str").getValue();
        HttpEntity entity = response.getEntity();
        String body = (entity != null && entity.isRepeatable()) ? EntityUtils.toString(entity) : "";;
        return timestamp + "\n"
                + nonce + "\n"
                + body + "\n";
    }

    /***
     * @description 构建notify信息
     * @param notifyRequest  推送请求结果
     * @return: java.lang.String 应答信息
     */
    public static String notifyMessage(NotifyRequest notifyRequest)  {
        String timestamp = notifyRequest.getTimestamp();
        String nonce = notifyRequest.getNonce();
        String body = notifyRequest.getBody();
        return timestamp + "\n"
                + nonce + "\n"
                + body + "\n";
    }

    public static void validateParametersNotify(NotifyRequest notifyRequest) {
        //请求中必须存在的内容：签名字符串，随机字符串，时间戳
        String signature = notifyRequest.getSignature();
        if (signature==null){
            throw new RuntimeException("签名字符串为空");
        }
        String nonce = notifyRequest.getNonce();
        if (nonce==null){
            throw new RuntimeException("随机字符串为空");
        }
        String timestamp = notifyRequest.getTimestamp();
        if (timestamp==null){
            throw new RuntimeException("时间戳为空");
        }
        //获得最后时间戳，判断应答时间戳到当前时间超过5分钟则认为应答失败
        try {
            Instant responseTime = Instant.ofEpochSecond(Long.parseLong(timestamp));
            // 拒绝过期应答
            if (Duration.between(responseTime, Instant.now()).abs().toMinutes() >= 5) {
                throw new RuntimeException("应答超时");
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw new RuntimeException("应答时间处理异常");
        }
    }
}
