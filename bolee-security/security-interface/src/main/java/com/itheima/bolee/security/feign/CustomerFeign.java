package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.security.hystrix.UserHtstrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：用户权限适配服务接口定义
 */
@FeignClient(value = "security-web",fallback = UserHtstrix.class)
public interface CustomerFeign {

    /**
     * @Description 按用客户查找用户
     * @param username 登录名
     * @return
     */
    @PostMapping("customer-feign/username-login/{username}/{company}")
    UserVO usernameLogin(@PathVariable("username") String username,@PathVariable("company") String company);

    /**
     * @Description 按用户手机查找用户
     * @param mobile 登录名
     * @return
     */
    @PostMapping("customer-feign/mobile-login/{mobile}/{company}")
    UserVO mobileLogin(@PathVariable("mobile") String mobile,@PathVariable("company") String company);

    /**
     * @Description 按微信openId查找客户
     * @param openId 登录名
     * @return
     */
    @PostMapping("customer-feign/wechat-login/{openId}/{company}")
    UserVO wechatLogin(@PathVariable("openId") String openId,@PathVariable("company") String company);

    /**
     * @Description 注册客户
     * @param customerVO 客户信息
     * @return
     */
    @PostMapping("customer-feign/register-user")
    UserVO registerUser(@RequestBody CustomerVO customerVO);

    /**
     * 重置密码
     * @param customerId
     * @return
     */
    @PostMapping("reset-passwords/{customerId}")
    Boolean resetPasswords(@PathVariable("customerId") String customerId);


    /**
     * 修改用户真实姓名
     * @param userId
     * @param name
     * @return
     */
    @PostMapping("customer-feign/update-real-name")
    Boolean updateRealName(@RequestParam("userId") String userId,@RequestParam("name")  String name);
}