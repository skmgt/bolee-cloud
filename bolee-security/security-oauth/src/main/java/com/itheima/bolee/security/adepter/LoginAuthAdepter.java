package com.itheima.bolee.security.adepter;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName LoginAuthAdepter.java
 * @Description 登录适配接口
 */
public interface LoginAuthAdepter {

    /***
     * @description 适配路由
     * @param principal    认证主体
     * @param parameters   登录参数
     * @return
     */
    UserVO adepterRoutes(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException;
}
