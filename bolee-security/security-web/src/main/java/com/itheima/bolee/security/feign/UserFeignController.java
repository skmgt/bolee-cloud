package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.dto.security.*;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.security.pojo.User;
import com.itheima.bolee.security.service.IResourceService;
import com.itheima.bolee.security.service.IRoleService;
import com.itheima.bolee.security.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserAdapterController.java
 * @Description 用户适配controller
 */
@RestController
@RequestMapping("user-feign")
@Api(tags = "用户feign管理")
@Slf4j
public class UserFeignController {

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IResourceService resourceService;

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
        return userService.usernameLogin(username,companyNo);

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
        return userService.mobileLogin(mobile,companyNo);

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
        return userService.wechatLogin(openId,companyNo);

    }

    /**
     * @Description 查找用户所有角色
     * @param userId 用户Id
     * @return
     */
    @PostMapping("find-role-user/{userId}")
    @ApiOperation(value = "查找用户所有角色",notes = "查找用户所有角色")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用户id",dataType = "Long")
    List<RoleVO> findRoleByUserId(@PathVariable("userId")Long userId){
        return roleService.findRoleVOListByUserId(userId);
    }

    /**
     * @Description 查询用户有资源
     * @param userId 用户Id
     * @return
     */
    @PostMapping("find-resoure-user/{userId}")
    @ApiOperation(value = "查询用户有资源",notes = "查询用户有资源")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用户id",dataType = "Long")
    List<ResourceVO> findResourceByUserId(@PathVariable("userId")Long userId){
        return resourceService.findResourceVOListByUserId(userId);
    }

    /***
     * @description 查询用户数据权限
     * @param queryDataSecurityVO 角色列表
     *
     * @return
     */
    @ApiOperation(value = "查询用户数据权限",notes = "查询用户数据权限")
    @ApiImplicitParam(name = "queryDataSecurityVO",value = "查询对象",required = true,dataType = "QueryDataSecurityVO")
    @PostMapping(value = "user-data-security")
    DataSecurityVO userDataSecurity(@RequestBody QueryDataSecurityVO queryDataSecurityVO ){
        return userService.userDataSecurity(queryDataSecurityVO.getRoleVOs(),queryDataSecurityVO.getUserId());
    }

    /***
     * @description 根据用户id查询出用户信息
     * @param userId 角色id
     * @return
     */
    @ApiOperation(value = "根据用户id获取对应的用户对象信息",notes = "根据用户id获取对应的用户对象信息")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "Long")
    @PostMapping(value = "find-user-by-id")
    UserVO userDataSecurity(@RequestParam("userId") Long userId){
        return BeanConv.toBean(userService.getById(userId),UserVO.class);
    }
}
