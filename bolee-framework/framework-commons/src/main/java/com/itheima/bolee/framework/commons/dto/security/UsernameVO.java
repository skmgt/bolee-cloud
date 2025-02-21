package com.itheima.bolee.framework.commons.dto.security;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName LoginVo.java
 * @Description 登录名称处理对象
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UsernameVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder
    public UsernameVO(String username, String clientId, String loginType, String loginBeanName,String companyNo) {
        this.username = username;
        this.clientId = clientId;
        this.loginType = loginType;
        this.loginBeanName = loginBeanName;
        this.companyNo = companyNo;
    }

    //登录名称
    private String username;

    //客户端
    private String clientId;

    //登录类型
    private String loginType;

    //登录处理类bean名称
    private String loginBeanName;

    //企业编号
    private String companyNo;

    /**
     * 用户姓名
     */
    private String realName;

}
