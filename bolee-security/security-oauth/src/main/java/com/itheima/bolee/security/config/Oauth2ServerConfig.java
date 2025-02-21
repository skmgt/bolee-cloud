package com.itheima.bolee.security.config;


import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.base.UserAuth;
import com.itheima.bolee.security.details.JdbcClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权服务配置
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService oauth2UserDetailsService;

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    /***
     * @description 客户端信息到数据库中
     * @param clients 客户端配置形象
     * @return
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /***
     * @description 客户端信息JDBC管理
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
        jdbcClientDetailsService.setFindClientDetailsSql(OauthConstant.FIND_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setSelectClientDetailsSql(OauthConstant.SELECT_CLIENT_DETAILS_SQL);
        return jdbcClientDetailsService;
    }

    /***
     * @description 授权服务器端点的非安全特性如令牌存储、令牌自定义、用户批准和授权类型
     * @param endpoints  认证服务令牌站点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
            .authenticationManager(authenticationManager)//配置认证管理器
            .userDetailsService(oauth2UserDetailsService)//配置oauth2User查询明细
            .accessTokenConverter(jwtAccessTokenConverter())//配置令牌转换器
            .tokenServices(jwtTokenService())//配置令牌服务(token services)
            .reuseRefreshTokens(true);//是否沿用刷新令牌直到过期，这里每次刷新直接后使用新的刷新令牌下次刷新
    }

    /**
     * 令牌服务：持久化令牌相关服务
     */
    @Bean
    public DefaultTokenServices jwtTokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        //存储策略
        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        service.setTokenStore(jwtTokenStore);//令牌存储的持久性策略。
        service.setClientDetailsService(jdbcClientDetailsService());//指定客戶端明细信息
        service.setSupportRefreshToken(true);//是否支持刷新令牌
        service.setTokenEnhancer(tokenEnhancerChain());//在将新令牌保存到令牌存储区之前，将应用于新令牌的访问令牌增强器。
        service.setAccessTokenValiditySeconds(securityConfigProperties.getAccessTokenValiditySeconds()); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(securityConfigProperties.getRefreshTokenValiditySeconds()); // 刷新令牌默认有效期3天
        return service;
    }


    /***
     * @description 复合令牌增强器:把构建的JWT令牌及增强属性填充到OAuth2AccessToken中
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain(){
        //token令牌增强者List
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        //扩展JWT内容增强，可携带更多对象信息：企业号、门店ID等
        tokenEnhancers.add(tokenEnhancer());
        //使用非对称加密算法对token签名
        tokenEnhancers.add(jwtAccessTokenConverter());
        //构建增强链：填入token令牌增强者List
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        return tokenEnhancerChain;
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //从classpath下的密钥库中获取密钥对(公钥+私钥)
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
                new ClassPathResource("itheima.jks"), "shuwenqi".toCharArray());
        KeyPair keyPair = factory.getKeyPair(
                "itheima", "shuwenqi".toCharArray());
        return keyPair;
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> map = new HashMap<>(10);
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof UserAuth){
                UserAuth userAuth = (UserAuth)principal ;
                map.put(OauthConstant.USER_ID_KEY, userAuth.getId());
                map.put(OauthConstant.REAL_NAME_KEY, userAuth.getRealName());
                map.put(OauthConstant.CLIENT_ID_KEY, userAuth.getClientId());
                map.put(OauthConstant.USER_NAME_KEY,userAuth.getUsername());
                map.put(OauthConstant.SEX_KEY,userAuth.getSex());
                map.put(OauthConstant.RESOURCS_KEY,userAuth.getResourceRequestPaths());
                map.put(OauthConstant.ROLES_KEY,userAuth.getRoleLabels());
                map.put(OauthConstant.OPEN_ID_KEY,userAuth.getOpenId());
                map.put(OauthConstant.DEPT_NO_KEY,userAuth.getDeptNo());
                map.put(OauthConstant.POST_NO_KEY,userAuth.getPostNo());
                map.put(OauthConstant.MOBILE_KEY,userAuth.getMobile());
                map.put(OauthConstant.ONLY_AUTHENTICATE_KEY,userAuth.getOnlyAuthenticate());
                map.put(OauthConstant.DATA_SECURITY_KEY,userAuth.getDataSecurityVO());
                map.put(OauthConstant.COMPANY_NO_KEY,userAuth.getCompanyNo());
                map.put(OauthConstant.EXPIRES_IN_KEY,securityConfigProperties.getAccessTokenValiditySeconds());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            }

            return accessToken;
        };
    }

    /***
     * @description  认证异常处理配置
     * @return:
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.authenticationEntryPoint(authenticationEntryPoint())//异常处理
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


    /***
     * @description 自定义认证异常响应数据
     * @return: org.springframework.security.web.AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            ResponseResult<Boolean> responseWrap = ResponseResultBuild.build(AuthEnum.NEED_LOGIN, false);
            String result = JSONObject.toJSONString(responseWrap);
            response.getWriter().print(result);
            response.getWriter().flush();
        };
    }
}
