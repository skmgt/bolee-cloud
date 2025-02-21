package com.itheima.bolee;

/**
 * @ClassName Signer.java
 * @Description 签名加密者
 */
public interface Signer {

    String sign(byte[] message);

}
