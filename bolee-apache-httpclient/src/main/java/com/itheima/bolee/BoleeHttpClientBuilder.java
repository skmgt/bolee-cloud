package com.itheima.bolee;

import com.itheima.bolee.auth.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.ClientExecChain;

/**
 * @ClassName BoleeHttpClientBuilder.java
 * @Description 保乐三方请求HttpClient对象构建
 */
public class BoleeHttpClientBuilder extends HttpClientBuilder {

    //系统相关属性
    private static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
    private static final String VERSION = System.getProperty("java.version");

    //凭证构建
    private Credentials credentials;

    //校验者
    private Validator validator;


    public static BoleeHttpClientBuilder create() {
        return new BoleeHttpClientBuilder();
    }

    private BoleeHttpClientBuilder() {
        String userAgent = String.format("Bolee-Apache-HttpClient/%s (%s) Java/%s", this.getClass().getPackage().getImplementationVersion(), OS, VERSION == null ? "Unknown" : VERSION);
        this.setUserAgent(userAgent);
    }

    public BoleeHttpClientBuilder withCredentials(String appId,String privateKey) {
        this.credentials = new BoleeCredentials(appId, new PrivateKeySigner(privateKey));
        return this;
    }


    public BoleeHttpClientBuilder withValidator(String publicKey) {
        this.validator = new BoleeValidator(new PublicKeyVerifier(publicKey));
        return this;
    }

    public CloseableHttpClient build() {
        if (this.credentials == null) {
            throw new IllegalArgumentException("缺少身份认证信息");
        } else if (this.validator == null) {
            throw new IllegalArgumentException("缺少签名验证信息");
        } else {
            return super.build();
        }
    }

    protected ClientExecChain decorateProtocolExec(ClientExecChain requestExecutor) {
        return new DecorateClientExecChain(this.credentials, this.validator, requestExecutor);
    }
}
