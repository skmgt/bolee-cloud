package com.itheima.bolee.dto;

import lombok.Data;

/**
 * RequestParamDTO
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Data
public class CredentialsDTO {

    /**
     * 请求方式
     */
    private String methodd;

    /**
     * 接口路径
     */
    private String uriPath;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 签名数据
     */
    private String signature;

    /**
     * 密文数据
     */
    private String securityBody;

    /**
     * 随机字符串
     */
    private String nonce;



    /**
     * 构建签名字符串
     *  return request.getRequestLine().getMethod() + "\n"
     *                 + canonicalUrl + "\n"
     *                 + timestamp + "\n"
     *                 + nonce + "\n"
     *                 + body + "\n";
     * @return
     */
    public String buildSignBody(){
        StringBuilder sb = new StringBuilder();
        sb.append(methodd + "\n");
        sb.append(uriPath + "\n");
        sb.append(timestamp + "\n");
        sb.append(nonce + "\n");
        sb.append(securityBody + "\n");
        return sb.toString();
    }
}
