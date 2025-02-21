package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.dto.security.*;
import com.itheima.bolee.security.hystrix.UserHtstrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：用户权限适配服务接口定义
 */
@FeignClient(value = "security-web",fallback = UserHtstrix.class)
public interface UserFeign {

    /**
     * @Description 按用户名查找用户
     * @param username 登录名
     * @return
     */
    @PostMapping("user-feign/username-login/{username}/{company}")
    UserVO usernameLogin(@PathVariable("username") String username,@PathVariable("company") String company);

    /**
     * @Description 按用户手机查找用户
     * @param mobile 登录名
     * @return
     */
    @PostMapping("user-feign/mobile-login/{mobile}/{company}")
    UserVO mobileLogin(@PathVariable("mobile") String mobile,@PathVariable("company") String company);

    /**
     * @Description 按微信openId查找客户
     * @param openId 登录名
     * @return
     */
    @PostMapping("user-feign/wechat-login/{openId}/{company}")
    UserVO wechatLogin(@PathVariable("openId") String openId,@PathVariable("company") String company);
    /**
     * @Description 查找用户所有角色
     * @param userId 用户Id
     * @return
     */
    @PostMapping("user-feign/find-role-user/{userId}")
    List<RoleVO> findRoleByUserId(@PathVariable("userId") Long userId);

    /**
     * @Description 查询用户有资源
     * @param userId 用户Id
     * @return
     */
    @PostMapping("user-feign/find-resoure-user/{userId}")
    List<ResourceVO> findResourceByUserId(@PathVariable("userId") Long userId);

    /***
     * @description 查询用户数据权限
     * @param queryDataSecurityVO 查询对象Vo
     *
     * @return
     */
    @PostMapping(value = "user-feign/user-data-security")
    DataSecurityVO userDataSecurity(@RequestBody QueryDataSecurityVO queryDataSecurityVO);


    /***
     * @description 查询用户数据权限
     * @param userId 用户id
     * @return
     */
    @PostMapping(value = "user-feign/find-user-by-id")
    UserVO findUserById(@RequestParam("userId") Long userId);
}
