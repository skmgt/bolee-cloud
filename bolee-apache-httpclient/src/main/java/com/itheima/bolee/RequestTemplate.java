package com.itheima.bolee;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.auth.BoleeEncryptor;
import com.itheima.bolee.constants.BoleeSecurityConstant;
import com.itheima.bolee.dto.CredentialsDTO;
import com.itheima.bolee.dto.EncodeDTO;
import com.itheima.bolee.utils.SecurityUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestTemplate
 *
 * @author: wgl
 * @describe: 对外统一发送模板--组装各个模组
 * 加密模组
 * 解密模组组装
 * @date: 2022/12/28 10:10
 */
@Data
public class RequestTemplate {

    private String privateKey;

    private String publicKey;

    private String appId;

    private URIBuilder uriBuilder;

    private RequestConfig requestConfig;

    @Builder
    public RequestTemplate(String privateKey, String publicKey, String appId, URIBuilder uriBuilder,RequestConfig requestConfig) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.appId = appId;
        this.uriBuilder = uriBuilder;
        this.requestConfig = requestConfig;
    }


    public <T> T doRequest(Object params,Class<T> t) throws URISyntaxException, IOException {
        CloseableHttpClient httpclient = BoleeHttpClientBuilder.create()
                .withCredentials(appId, privateKey)
                .withValidator(publicKey)
                .build();
        //=============基础配置===================
        RequestConfig requestConfig = null;
        if(ObjectUtil.isNull(requestConfig)) {
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(100000)
                    .setConnectTimeout(100000)
                    .build();
        }else{
            requestConfig = this.requestConfig;
        }
        try {
            URI url = uriBuilder.build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            EncodeDTO encodeDTO = new EncodeDTO();
            JSONObject body = JSONUtil.parseObj(params);
            encodeDTO.setBody(body.toString());
            if(new BoleeEncryptor(privateKey).encode(encodeDTO)){
                StringEntity stringEntity = new StringEntity(encodeDTO.getEncodeBody(), "utf-8");
                httpPost.setEntity(stringEntity);
                httpPost.addHeader(BoleeSecurityConstant.HEAD_NAME_BODY_KEY,encodeDTO.getHeadAESSecurityKey());
                httpPost.addHeader("Content-Type","application/json;charset=utf-8");
                CloseableHttpResponse apiRes = httpclient.execute(httpPost);
                //验签完毕后需要对数据进行解密
                HttpEntity entity = apiRes.getEntity();
                String content = EntityUtils.toString(entity);
                String responseJson = decryptRequestBody(content, apiRes.getFirstHeader(BoleeSecurityConstant.HEAD_NAME_BODY_KEY).getValue());
                if(StrUtil.isEmpty(responseJson)){
                    throw new RuntimeException("参数解析为空");
                }
                return JSONUtil.toBean(responseJson, t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * 解密请求body体
     *
     * @param dody 返回数据对象
     * @param aesKeyRSA 获取请求头中加密后的AES密钥
     * @return
     */
    private String decryptRequestBody(String dody, String aesKeyRSA) {
        try {
            //先使用rsa解密aes秘钥
            RSA rsa = SecureUtil.rsa(null, publicKey);
            String aesKey = SecurityUtil.decryptFromStringRSAPublicKey(rsa, aesKeyRSA);
            //使用aes进行解密
            // 使用解密后的AES密钥进行AES解密请求体数据
            String requestBody = SecurityUtil.decryptFromStringAES(dody, Mode.CBC, Padding.ZeroPadding,aesKey);
            return requestBody;
        } catch (Exception e) {
            e.printStackTrace();
            // 解密失败，可以根据你的需求进行异常处理
            return null;
        }
    }

    /**
     * 构建请求参数
     * 1694953272
     * xHct36JgHbj6tXBx
     * Modified: {"msg":"ok","code":"100","data":null}
     *
     * @param authorization
     * @param response
     * @return
     */
    private CredentialsDTO buildRequestParam(String authorization, CloseableHttpResponse response) {
        try {
            authorization = authorization.replaceAll("\"", "");
            String[] authorizations = authorization.split(",");
            Map<String, String> authMap = new HashMap<>();
            for (String authorizationIndex : authorizations) {
                String[] split = authorizationIndex.split("=");
                authMap.put(split[0], split[1].substring(0, split[1].length()));
            }
            CredentialsDTO credentialsDTO = new CredentialsDTO();
            credentialsDTO.setNonce(authMap.get("nonce_str"));
            credentialsDTO.setTimestamp(authMap.get("timestamp"));
            credentialsDTO.setSignature(authMap.get("signature"));
//            credentialsDTO.setSecurityBody(getPostData(req));
            return credentialsDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
