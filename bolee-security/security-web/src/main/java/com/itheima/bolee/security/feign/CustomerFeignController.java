package com.itheima.bolee.security.feign;

import com.itheima.bolee.security.service.ICustomerService;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName CustomerAdapterController.java
 * @Description 客户适配controller
 */
@RestController
@RequestMapping("customer-feign")
@Api(tags = "客户feign管理")
@Slf4j
public class CustomerFeignController {

    @Autowired
    ICustomerService customerService;

    /**
     * @Description 按用户名查找用户
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "按用户名查找用户",notes = "按用户名查找用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "username",value = "用户名称",dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "companyNo",value = "企业号",dataType = "String")
    })
    @PostMapping("username-login/{username}/{companyNo}")
    UserVO usernameLogin(@PathVariable("username") String username,@PathVariable("companyNo") String companyNo){
        return customerService.usernameLogin(username,companyNo);

    }

    /**
     * @Description 按用户手机查找用户
     * @param mobile 登录名
     * @return
     */
    @ApiOperation(value = "按用户手机查找用户",notes = "按用户手机查找用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "mobile",value = "用户手机",dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "companyNo",value = "企业号",dataType = "String")
    })
    @PostMapping("mobile-login/{mobile}/{companyNo}")
    UserVO mobileLogin(@PathVariable("mobile")String mobile,@PathVariable("companyNo") String companyNo){
        return customerService.mobileLogin(mobile,companyNo);

    }

    /**
     * @Description 按用户openid查找用户
     * @param openId 登录名
     * @return
     */
    @ApiOperation(value = "按用户openid查找用户",notes = "按用户openid查找用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "openId",value = "用户openid",dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "companyNo",value = "企业号",dataType = "String")
    })
    @PostMapping("wechat-login/{openId}/{companyNo}")
    UserVO wechatLogin(@PathVariable("openId") String openId,@PathVariable("companyNo") String companyNo){
        return customerService.wechatLogin(openId,companyNo);
    }


    /**
     * @Description 注册
     * @param customerVO 客户信息
     * @return
     */
    @ApiOperation(value = "注册客户",notes = "注册客户")
    @ApiImplicitParam(name = "customerVO",value = "客户Vo对象",required = true,dataType = "CustomerVO")
    @PostMapping("register-user")
    UserVO registerUser(@RequestBody CustomerVO customerVO){
        return BeanConv.toBean(customerService.createCustomer(customerVO),UserVO.class);
    }

    /**
     * 修改用户真实姓名
     * @param userId
     * @param name
     * @return
     */
    @PostMapping("update-real-name")
    Boolean updateRealName(@RequestParam("userId") String userId,@RequestParam("name")  String name){
        return customerService.updateRealName(userId,name);
    }
}
