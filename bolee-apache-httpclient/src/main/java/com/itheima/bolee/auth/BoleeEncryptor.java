package com.itheima.bolee.auth;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.itheima.bolee.Encryptor;
import com.itheima.bolee.constants.BoleeSecurityConstant;
import com.itheima.bolee.dto.EncodeDTO;
import com.itheima.bolee.utils.ClientUtils;
import com.itheima.bolee.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * BoleeEncryptor
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Slf4j
public class BoleeEncryptor implements Encryptor {

    //私钥签名
    protected String privateKeyBase64;


    public BoleeEncryptor(String privateKey) {
        this.privateKeyBase64 = privateKey;
    }

    /**
     * 对数据进行加密
     * @param body 请求消息体
     * @return
     * @throws IOException
     */
    @Override
    public boolean encode(EncodeDTO body){
        //获取AES加密秘钥
        String bodySecurity = ClientUtils.generateNonceStr();
        //使用AES加密 body体里的数据
        String aesSecurity = SecurityUtil.encryptFromStringAES(body.getBody(), Mode.CBC, Padding.ZeroPadding,bodySecurity);
        body.setEncodeBody(aesSecurity);
        //使用RSA加密AES的对称加密秘钥
        RSA rsa = new RSA(privateKeyBase64, null);
        String securityToken = SecurityUtil.encryptFromStringRSAPrivateKey(rsa, bodySecurity);
        body.setHeadAESSecurityKey(securityToken);
        //将新生产的AES的body返回
        return true;
    }
}