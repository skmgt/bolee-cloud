package com.itheima.bolee.auth;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.itheima.bolee.Verifier;
import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName PublicKeyVerifier.java
 * @Description 公钥验证
 */
public class PublicKeyVerifier implements Verifier {

    protected final String publicKey;

    public PublicKeyVerifier(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean verify(byte[] data,byte[] signature){
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA,null, Base64.decodeBase64(publicKey));
        return sign.verify(data,signature);
    }
}
