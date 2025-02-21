package com.itheima.bolee.framework.feign.interceptor;

import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @ClassName FeignAuthRequestInterceptor.java
 * @Description feign请求授权拦截器
 */
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        if (!EmptyUtil.isNullOrEmpty(SubjectContent.getUserToken())){
            template.header(SecurityConstant.USER_TOKEN, SubjectContent.getUserToken());
        }

    }
}
