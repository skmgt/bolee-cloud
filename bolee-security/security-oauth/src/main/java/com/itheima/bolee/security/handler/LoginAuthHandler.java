package com.itheima.bolee.security.handler;

import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName LoginAdapter.java
 * @Description 登陆处理器接口
 */
public interface LoginAuthHandler {

    /***
     * @description 登录处理
     * @param parameters   登录参数
     * @return
     */
    UserVO loginHandler(Principal principal, Map<String, String> parameters,
                         String loginBeanName, CompanyVO companyVO)
            throws HttpRequestMethodNotSupportedException;

    /***
     * @description 用户信息查询
     * @param loginType 登录类型
     * @param username  账号信息：用户名或者手机号或者openId
     * @param companyNo 企业号
     * @return: com.itheima.easy.vo.security.UserVO
     */
    UserVO findUserDetail(String loginType,String username,String companyNo);

}
