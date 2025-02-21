package com.itheima.bolee.security.adepter.impl;

import com.itheima.bolee.framework.commons.constant.security.CompanyCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.security.handler.LoginAuthHandler;
import com.itheima.bolee.security.adepter.LoginAuthAdepter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @ClassName LoginAuthAdepterImpl.java
 * @Description 登录适配器
 */
@Component
public class LoginAuthAdepterImpl implements LoginAuthAdepter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public UserVO adepterRoutes(Principal principal, Map<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        //获得域名
        String host = httpServletRequest.getHeaders("x-forwarded-host").nextElement().split(",")[0].split(":")[0];
        String key = CompanyCacheConstant.WEBSITE+host;
        //域名校验
        RBucket<CompanyVO> bucket = redissonClient.getBucket(key);
        CompanyVO companyVO = bucket.get();
        if (EmptyUtil.isNullOrEmpty(companyVO)){
            throw  new ProjectException(AuthEnum.HSOT_FAIL);
        }
        //适配登录方式
        String loginType = parameters.get(OauthConstant.LOGIN_TYPE_KEY);
        if (EmptyUtil.isNullOrEmpty(loginType)){
            throw  new ProjectException(AuthEnum.LOGIN_FAIL);
        }
        String loginBeanName = OauthConstant.loginBeanNames.get(loginType.split("-")[1]);
        LoginAuthHandler loginAuthHandler = registerBeanHandler.getBean(loginBeanName,LoginAuthHandler.class);
        return loginAuthHandler.loginHandler(principal,parameters,loginBeanName,companyVO);
    }
}
