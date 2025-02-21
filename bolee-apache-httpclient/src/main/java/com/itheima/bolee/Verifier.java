package com.itheima.bolee;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName Verifier.java
 * @Description 验签解密者
 */
public interface Verifier {

    boolean verify(byte[] data,byte[] signature);


}
