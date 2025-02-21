package com.itheima.bolee.security.handler.impl;

import com.itheima.bolee.framework.commons.constant.security.OauthCacheConstant;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.security.handler.CacheTokenHandler;
import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CacheTokenHandlerImpl.java
 * @Description Token缓存处理
 */
@Component
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class CacheTokenHandlerImpl implements CacheTokenHandler {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Override
    public UserVO cacheToken(OAuth2AccessToken oAuth2AccessToken){
        //登录成功后获得oAuth2AccessToken中增强信息
        Long id = Long.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.USER_ID_KEY).toString());
        String userToken =String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.JTI_KEY));
        String mobile =String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.MOBILE_KEY));
        String openId =String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.OPEN_ID_KEY));
        Set<String> resources = (Set<String>) oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.RESOURCS_KEY);
        Set<String> roles = (Set<String>) oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.ROLES_KEY);
        String username = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.USER_NAME_KEY));
        String sex = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.SEX_KEY));
        String clientId = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.CLIENT_ID_KEY));
        String realName = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.REAL_NAME_KEY));
        String deptNo = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.DEPT_NO_KEY));
        String postNo = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.POST_NO_KEY));
        Boolean onlyAuthenticate = (Boolean) oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.ONLY_AUTHENTICATE_KEY);
        DataSecurityVO dataSecurityVO = (DataSecurityVO) oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.DATA_SECURITY_KEY);
        String companyNo = String.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.COMPANY_NO_KEY));
        //设置accessToken到redis，获得时使用userToken获取
        String accessToken = oAuth2AccessToken.getValue();
        RBucket<String> accessTokenBucket = redissonClient.getBucket(OauthCacheConstant.ACCESS_TOKEN + userToken);
        long accessTokenExpiresIn = Long.valueOf(oAuth2AccessToken.getAdditionalInformation().get(OauthConstant.EXPIRES_IN_KEY).toString());
        accessTokenBucket.set(accessToken,accessTokenExpiresIn, TimeUnit.SECONDS);
        //设置refreshToken到redis，获得时使用userToken获取
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
        String refreshToken = oAuth2RefreshToken.getValue();
        long refreshTokenExpiresIn = securityConfigProperties.getRefreshTokenValiditySeconds();
        RBucket<String> refreshTokenBucket = redissonClient.getBucket(OauthCacheConstant.REFRESH_TOKEN + userToken);
        refreshTokenBucket.set(refreshToken,refreshTokenExpiresIn, TimeUnit.SECONDS);
        //绑定username与userToken,用于剔除设置
        RBucket<String> usernameBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN_BIND + username);
        usernameBucket.set(userToken,refreshTokenExpiresIn, TimeUnit.SECONDS);
        //构建返回对象
        UserVO userVO = UserVO.builder()
            .id(id)
            .clientId(clientId)
            .companyNo(companyNo)
            .username(username)
            .userToken(userToken)
            .resourceRequestPaths(resources)
            .roleLabels(roles)
            .openId(openId)
            .mobile(mobile)
            .deptNo(deptNo)
            .postNo(postNo)
            .dataSecurityVO(dataSecurityVO)
            .onlyAuthenticate(onlyAuthenticate)
            .sex(sex)
            .realName(realName)
            .build();
        //设置userVO到redis，获得时候使用userToken获得
        RBucket<UserVO> userTokenBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN + userToken);
        userTokenBucket.set(userVO,refreshTokenExpiresIn, TimeUnit.SECONDS);
        return userVO;
    }

    @Override
    public Boolean deleteToken(UserVO userVO) {
        //删除accessToken
        RBucket<String> accessTokenBucket = redissonClient.getBucket(OauthCacheConstant.ACCESS_TOKEN + userVO.getUserToken());
        boolean deleteAccessToken = accessTokenBucket.delete();
        //删除refreshToken
        RBucket<String> refreshTokenBucket = redissonClient.getBucket(OauthCacheConstant.REFRESH_TOKEN + userVO.getUserToken());
        boolean deleteRefreshToken = refreshTokenBucket.delete();
        //删除绑定username
        RBucket<String> usernameBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN_BIND + userVO.getUsername());
        boolean deleteUsername = usernameBucket.delete();
        //删除用户和userToken关联
        RBucket<UserVO> userTokenBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN + userVO.getUserToken());
        boolean deleteUserTokenBucket = userTokenBucket.delete();
        //清理完成
        if (deleteAccessToken&&deleteRefreshToken&&deleteUsername&&deleteUserTokenBucket){
            return true;
        }else {
            return false;
        }
    }

}
