package com.itheima.bolee.framework.intercept;

import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CompanyCheckIntercept.java
 * @Description 企业请求appSecret的安全校验
 */
@Component
public class CompanyCheckIntercept implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从头部中拿到当前appSecret
        String appSecret = request.getHeader(SecurityConstant.APP_SECRET);
        //校验
        return true;
    }

}
