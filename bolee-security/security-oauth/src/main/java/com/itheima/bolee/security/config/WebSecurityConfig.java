package com.itheima.bolee.security.config;

import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName ReactiveSecurityConfig.java
 * @Description 支持web的权限配置
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Autowired
    UserDetailsService oauth2UserDetailsService;

    /**
     * BCrypt密码编码
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义身份认证逻辑
     * @return
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定用户明细查询及密码处理者
        auth.userDetailsService(oauth2UserDetailsService).passwordEncoder(bcryptPasswordEncoder());
        super.configure(auth);
    }

    /**
     * 认证管理器
     * @return
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //使用默认的认证管理器
        return super.authenticationManagerBean();
    }

    /***
     * @description 过滤器链定义
     * @param http
     * @return
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()//站点请求处理
            .and()
            .authorizeRequests()
            .antMatchers(securityConfigProperties.getIgnoreUrl()
            .toArray(new String[securityConfigProperties.getIgnoreUrl().size()]))//忽略配置
            .permitAll()
            .anyRequest().authenticated()//其他请求都需要校验
            .and()
            .csrf().disable();
    }

}
