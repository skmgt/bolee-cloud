package com.itheima.bolee;

import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.auth.BoleeEncryptor;
import com.itheima.bolee.auth.PrivateKeySigner;
import com.itheima.bolee.auth.PublicKeyVerifier;
import com.itheima.bolee.constants.BoleeSecurityConstant;
import com.itheima.bolee.dto.EncodeDTO;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BoleeHttpClientTest.java
 * @Description TODO
 */
public class BoleeHttpClientTest {

    private static String privateKeyBase64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAILdGdmrbwdn0tkD5I1RYB/whGEUsAOYprsy/F0cCdRFcUfNT6Q4BcErwcJAmdXPDuSYSbPsImKA0EbLN+pGh/wwlrYtBueItE4zGV2GdMNhAU3P6CquIAm5H58UFeDmVEUnE81laylXjaiaZduXBVTiLgTPVcpSxus70ZJQQirBAgMBAAECgYAg0sBHHn7MxrfWAunyoDSSDkvF5eB4JnO7hIBUAlJc0cYmElMlh3+6AfWpeXaccED2CVSDMnk1Z8XV2+b8dhBpTo3IHG+IQwZvz89bfMcwhhpRT9eXpC5YkG5UHlh11Ftm7byAo7s0HG/JynquxWZDRifWnvrA2bYE2sk6oN2zuQJBAMxlxcDDcSIdsZbXH7zTFJ6c0WEYlsXndRJhT1OfmxzwNitN+8PicbyJh6GfDcM+PR+13rqGZ4x+yGyKSjsp9XMCQQCj5tRLINjRUcNalPrJnvvUMhlJ0yrxsKReX81L1lwDXjbFpcMsy9eZddgdLjGXRgH7K9tYmLGZ6hflRGTcbrH7AkEAt/HDFOYOU1iLsKbrDgCcJt4T5CC/11ykVCU0wZn6ewGGjlRBBhksqDLQ19ePCC1jzrzas9wvJhYXAu81PKdXFwJAUftq4u1aJlFcgtmUG/efBUPN7GRozZ3Kib4nxTBCtBiTEwfX+Xc4r3UHlYj+mykUYptMSyONamxyaWZtgOkJswJBAMLjyUR61F4UPjoyGGg5G4eRTM3fJ1w7ulHYYxlEy5u2UAC/jBtd07wv030lsqPSA7abTdK+6iNM2v0gA7KODKk=";

    private static String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC3RnZq28HZ9LZA+SNUWAf8IRhFLADmKa7MvxdHAnURXFHzU+kOAXBK8HCQJnVzw7kmEmz7CJigNBGyzfqRof8MJa2LQbniLROMxldhnTDYQFNz+gqriAJuR+fFBXg5lRFJxPNZWspV42ommXblwVU4i4Ez1XKUsbrO9GSUEIqwQIDAQAB";

    @Test
    public void testTemplate() throws URISyntaxException, IOException {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost:8080")
                .setPath("/insure");
        RequestTemplate requestTemplate = RequestTemplate.builder()
                .appId("10001")
                .privateKey(privateKeyBase64)
                .publicKey(publicKeyBase64)
                .uriBuilder(urlBuilder)
                .build();
        Map<String, Object> params = new HashMap<>();
        params.put("name","张三");
        params.put("age","18");
        params.put("sex","男");
        Map map = requestTemplate.doRequest(params, Map.class);
        System.out.println(map);
    }




    @Test
    public void BoleeHttpClientBuilderTest(){
        CloseableHttpClient httpclient = BoleeHttpClientBuilder.create()
                .withCredentials("10001", privateKeyBase64)
                .withValidator(publicKeyBase64)
                .build();
        //=============基础配置===================
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost:8080")
                .setPath("/insure");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .build();
        try {
            URI url = urlBuilder.build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            EncodeDTO encodeDTO = new EncodeDTO();
            JSONObject body = JSONUtil.parseObj(new HashMap<>() {
                {
                    put("age", 19);
                }
            });
            encodeDTO.setBody(body.toString());
            if(new BoleeEncryptor(privateKeyBase64).encode(encodeDTO)){
                StringEntity stringEntity = new StringEntity(encodeDTO.getEncodeBody(), "utf-8");
                httpPost.setEntity(stringEntity);
                httpPost.addHeader(BoleeSecurityConstant.HEAD_NAME_BODY_KEY,encodeDTO.getHeadAESSecurityKey());
                httpPost.addHeader("Content-Type","application/json;charset=utf-8");
                CloseableHttpResponse apiRes = httpclient.execute(httpPost);
                HttpEntity entity = apiRes.getEntity();
                String content = EntityUtils.toString(entity);
                System.out.println("接收到了返回信息:"+content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
