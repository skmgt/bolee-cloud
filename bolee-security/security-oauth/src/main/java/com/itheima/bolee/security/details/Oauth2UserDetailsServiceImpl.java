package com.itheima.bolee.security.details;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.*;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.security.feign.UserFeign;
import com.itheima.bolee.security.handler.LoginAuthHandler;
import com.itheima.bolee.security.base.UserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName ReactiveUserDetailsServiceImpl.java
 * @Description 支持flum的身份类实现ReactiveUserDetailsService接口
 */
@Slf4j
@Component("oauth2UserDetailsService")
public class Oauth2UserDetailsServiceImpl implements UserDetailsService {

    //调用RPC原创服务
    @Autowired
    UserFeign userFeign;

    //调用RPC原创服务
    @Autowired
    CustomerFeign customerFeign;

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        UsernameVO usernameVO = JSONObject.parseObject(principal,UsernameVO.class);
        //查询用户明细
        LoginAuthHandler loginAuthHandler = registerBeanHandler
            .getBean(usernameVO.getLoginBeanName(),LoginAuthHandler.class);
        UserVO userVO = loginAuthHandler.findUserDetail(usernameVO.getLoginType(),
                usernameVO.getUsername(),usernameVO.getCompanyNo());
        //UserAuth构建
        if (EmptyUtil.isNullOrEmpty(userVO)) {
            throw new DisabledException("无效的账号");
        }
        if (SuperConstant.DATA_STATE_1.equals(userVO.getDataState())) {
            throw new LockedException("账户被禁用");
        }
        //客户端
        userVO.setClientId(usernameVO.getClientId());
        //资源
        List<ResourceVO> resourceVOs = userFeign.findResourceByUserId(userVO.getId());
        if (!EmptyUtil.isNullOrEmpty(resourceVOs)){
            Set<String> resources = resourceVOs.stream().map(ResourceVO::getRequestPath).collect(Collectors.toSet());
            userVO.setResourceRequestPaths(resources);
        }
        //角色
        List<RoleVO> roleVOs = userFeign.findRoleByUserId(userVO.getId());
        if (!EmptyUtil.isNullOrEmpty(roleVOs)){
            Set<String> roleLabel = roleVOs.stream().map(RoleVO::getLabel).collect(Collectors.toSet());
            userVO.setRoleLabels(roleLabel);
        }
        //数据权限
        QueryDataSecurityVO queryDataSecurityVO = QueryDataSecurityVO.builder()
            .userId(userVO.getId())
            .roleVOs(roleVOs)
            .build();
        DataSecurityVO dataSecurityVO = userFeign.userDataSecurity(queryDataSecurityVO);
        if (!EmptyUtil.isNullOrEmpty(dataSecurityVO)){
            userVO.setDataSecurityVO(dataSecurityVO);
        }

        return new UserAuth(userVO);
    }
}
