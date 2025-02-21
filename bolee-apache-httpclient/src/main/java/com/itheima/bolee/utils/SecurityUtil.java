package com.itheima.bolee.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName SymmetricCryptoUtil.java
 * @Description TODO
 */
public class SecurityUtil {

    /**
     * 16字节
     */
    private static final String ENCODE_KEY = "1234567812345678";
    private static final String IV_KEY = "0000000000000000";

    public static void main(String[] args) {
        String password = "zdm321123.";
        String encryptData = encryptFromStringAES(password, Mode.CBC, Padding.ZeroPadding);
        System.out.println("AES加密：" + encryptData);
        String decryptData = decryptFromStringAES(encryptData, Mode.CBC, Padding.ZeroPadding);
        System.out.println("AES解密：" + decryptData);
        RSA rsa = new RSA();
        encryptData = encryptFromStringRSA(rsa,password);
        System.out.println("RSA加密："+encryptData);
        System.out.println("================用于网络传输===========");
        decryptData = decryptFromStringRSA(rsa,encryptData);
        //解密内容生成字符串
        System.out.println("RSA解密：" + decryptData);

    }

    public static String encryptFromStringAES(String data, Mode mode, Padding padding) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(IV_KEY.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        return aes.encryptBase64(data, StandardCharsets.UTF_8);
    }

    public static String encryptFromStringAES(String data, Mode mode, Padding padding,String securityKey) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(securityKey.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        return aes.encryptBase64(data, StandardCharsets.UTF_8);
    }

    public static String encryptFromStringRSA(RSA rsa,String password) {
        //byte[]内容加密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(password, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        //加密后base64处理用于网络传输
        return Base64.encodeBase64String(encrypt);
    }

    public static String encryptFromStringRSAPrivateKey(RSA rsa,String password) {
        //byte[]内容加密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(password, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        //加密后base64处理用于网络传输
        return Base64.encodeBase64String(encrypt);
    }

    public static String decryptFromStringAES(String data, Mode mode, Padding padding) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(IV_KEY.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        byte[] decryptDataBase64 = aes.decrypt(data);
        return new String(decryptDataBase64, StandardCharsets.UTF_8);
    }

    public static String decryptFromStringAES(String data, Mode mode, Padding padding,String aesKey) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(aesKey.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        byte[] decryptDataBase64 = aes.decrypt(data);
        return new String(decryptDataBase64, StandardCharsets.UTF_8);
    }

    public static String decryptFromStringRSA(RSA rsa, String encryptData) {
        //对byte数组进行解密
        byte[] decrypt = rsa.decrypt(Base64.decodeBase64(encryptData), KeyType.PrivateKey);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public static String decryptFromStringRSAPublicKey(RSA rsa, String encryptData) {
        //对byte数组进行解密
        byte[] decrypt = rsa.decrypt(Base64.decodeBase64(encryptData), KeyType.PublicKey);
        return new String(decrypt, StandardCharsets.UTF_8);
    }
}
