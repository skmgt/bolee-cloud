package com.itheima.bolee.auth;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.itheima.bolee.Signer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName PrivateKeySigner.java
 * @Description TODO
 */
@Slf4j
public class PrivateKeySigner implements Signer {

    protected final String privateKey;

    public PrivateKeySigner(String privateKey) {
        this.privateKey = privateKey;
    }


    @Override
    public String sign(byte[] message) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA,Base64.decodeBase64(privateKey),null);
        return Base64.encodeBase64String(sign.sign(message));
    }
}