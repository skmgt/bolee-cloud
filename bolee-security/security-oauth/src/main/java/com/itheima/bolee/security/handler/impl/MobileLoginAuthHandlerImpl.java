package com.itheima.bolee.security.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.security.OauthCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.dto.security.UsernameVO;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.security.handler.CacheTokenHandler;
import com.itheima.bolee.security.handler.LoginAuthHandler;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.security.feign.UserFeign;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName MobileLoginAuthHandlerImpl.java
 * @Description 手机验证码登录处理器实现
 */
@Slf4j
@Component("mobileLoginAuthHandler")
public class MobileLoginAuthHandlerImpl implements LoginAuthHandler {

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    CacheTokenHandler cacheTokenHandler;

    //调用RPC原创服务
    @Autowired
    UserFeign userFeign;

    //调用RPC原创服务
    @Autowired
    CustomerFeign customerFeign;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserVO loginHandler(Principal principal, Map<String, String> parameters,
                                String loginBeanName, CompanyVO companyVO)
            throws HttpRequestMethodNotSupportedException {
        String mobile = parameters.get(OauthConstant.MOBILE_KEY);
        String clientId = parameters.get(OauthConstant.CLIENT_ID_KEY);
        String loginType = parameters.get(OauthConstant.LOGIN_TYPE_KEY);
        UsernameVO usernameVO = UsernameVO.builder()
            .username(mobile)
            .clientId(clientId)
            .companyNo(companyVO.getCompanyNo())
            .loginType(loginType)
            .loginBeanName(loginBeanName)
            .build();
        String username = JSONObject.toJSONString(usernameVO);
        parameters.put(OauthConstant.USER_NAME_KEY,username);
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return cacheTokenHandler.cacheToken(oAuth2AccessToken);
    }

    @Override
    public UserVO findUserDetail(String loginType, String mobile,String companyNo) {
        //手机登录处理
        String key = OauthCacheConstant.LOGIN_CODE+mobile;
        //存储手机发送的验证码到存在中
//        RBucket<String> code = redissonClient.getBucket(key);
//        if (EmptyUtil.isNullOrEmpty(code.get())){
//            throw  new ProjectException(AuthEnum.CODE_FAIL);
//        }
//        String password = bCryptPasswordEncoder.encode(code.get());
        String password = bCryptPasswordEncoder.encode("123456");
        //处理登录
        UserVO userVO = null;
        switch (loginType){
            case OauthConstant.USER_MOBILE:
                userVO = userFeign.mobileLogin(mobile,companyNo);
                if (!EmptyUtil.isNullOrEmpty(userVO)){
                    userVO.setPassword(password);
                    userVO.setOnlyAuthenticate(false);
                }
                return userVO;
            case OauthConstant.CUSTOMER_MOBILE:
                userVO = customerFeign.mobileLogin(mobile,companyNo);
                if (!EmptyUtil.isNullOrEmpty(userVO)){
                    userVO.setPassword(password);
                    userVO.setOnlyAuthenticate(true);
                }
                return userVO;
            default:
                throw new ProjectException(AuthEnum.LOGIN_FAIL);
        }
    }
}
