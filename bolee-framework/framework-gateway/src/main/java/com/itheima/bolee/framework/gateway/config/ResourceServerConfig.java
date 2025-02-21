package com.itheima.bolee.framework.gateway.config;

import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.gateway.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

/**
 * @ClassName ResourceServerConfig.java
 * @Description 资源服务器配置
 */
@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class ResourceServerConfig {

    @Autowired
    private ReactiveAuthorizationManager jwtReactiveAuthorizationManager;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    /***
     * @description 跨域处理
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        return httpServletRequest -> {
            //初始化跨域配置
            CorsConfiguration cfg = new CorsConfiguration();
            //请求头中可以包含任意的参数
            cfg.addAllowedHeader("*");
            //请求方式可以是任意方式：post get patch pu delete opertions
            cfg.addAllowedMethod("*");
            //指定请求源的目标地址
            securityConfigProperties.getOrigins().forEach(origin->{
                cfg.addAllowedOrigin(origin);
            });
            cfg.setAllowCredentials(true);
            return cfg;
        };
    }

    /***
     * @description 鉴权过滤器链
     * @param http 服务器鉴权请求
     * @return 过滤器链
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        //jwt鉴权转换器
        http.oauth2ResourceServer().jwt();
        http.authorizeExchange()
            //匿名资源放行
            .pathMatchers(securityConfigProperties.getIgnoreUrl()
                    .toArray(new String[securityConfigProperties.getIgnoreUrl().size()])).permitAll()
            // 访问权限控制
            .anyExchange().access(jwtReactiveAuthorizationManager)
            .and()
            .exceptionHandling()
            //处理未授权
            .accessDeniedHandler(accessDeniedHandler())
            //处理未认证
            .authenticationEntryPoint(authenticationEntryPoint())
            .and().csrf().disable();
        return http.build();
    }

    /***
     * @description 未授权处理
     * @return 过滤器链
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> WebUtils.writeFailedToResponse(response, AuthEnum.AUTH_FAIL));
            return mono;
        };
    }

    /**
     * token无效或者已过期自定义响应
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, e) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> WebUtils.writeFailedToResponse(response,AuthEnum.NEED_LOGIN));
            return mono;
        };
    }


}
