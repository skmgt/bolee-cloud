package com.itheima.bolee.security.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.security.adepter.LoginAuthAdepter;
import com.itheima.bolee.security.handler.LogoutHandler;
import com.itheima.bolee.security.handler.RefreshTokenHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Oauth2获取令牌接口
 */
@RestController
@RequestMapping("/oauth")
@Api(tags = "登录认证")
public class AuthController {

    @Autowired
    private LoginAuthAdepter loginAuthAdepter;

    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired
    private RefreshTokenHandler refreshTokenHandler;

    /**
     * Oauth2登录认证
     * 登录必传：
     *  client_id：客户端
     *  client_secret：客户端秘钥
     *  grant_type：登录传入：password
     *  login_type：OauthConstant中选择：USER_USERNAME、USER_MOBILE、CUSTOMER_USERNAME、CUSTOMER_MOBILE
     *  username:用户账号【用户名、手机号】
     *  password:用户密码【明文密码、手机验证码】
     *  code:三方授权登录传入的授权码
     * 刷新必传：
     *  client_id：客户端
     *  grant_type:refresh_token
     *  client_secret：客户端秘钥
     *  jti:本次回话标识
     */
    @ApiOperation(value = "登录认证",notes = "登录认证")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "grant_type", value = "授权模式",  example = "password",required = true),
        @ApiImplicitParam(name = "client_id",  value = "Oauth2客户端ID",example = "operators-pc", required = true),
        @ApiImplicitParam(name = "client_secret",value = "Oauth2客户端秘钥",  example = "pass", required = true),
        @ApiImplicitParam(name = "username",  value = "登录用户名",example = "admin@qq.com"),
        @ApiImplicitParam(name = "mobile", value = "登录用户名", example = "15156403088"),
        @ApiImplicitParam(name = "usetToken",value = "会话usetToken",required = true),
        @ApiImplicitParam(name = "password", value = "登录密码", example = "pass"),
        @ApiImplicitParam(name = "login_type", value = "登录类型", example = "详解OauthConstant登录类型"),
        @ApiImplicitParam(name = "code", value = "小程序code")
    })
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseResult<UserVO> postAccessToken(@ApiIgnore Principal principal,
        @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        UserVO userVO = null;
        boolean isRefreshToken = OauthConstant.GRANT_TYPE_REFRESH_TOKEN
                .equals(parameters.get(OauthConstant.GRANT_TYPE_KEY));
        //令牌刷新
        if (isRefreshToken){
            userVO =refreshTokenHandler.refreshToken(principal, parameters);
        //用户登录
        }else {
            userVO = loginAuthAdepter.adepterRoutes(principal, parameters);
        }
        return ResponseResultBuild.build(BaseEnum.SUCCEED, userVO);
    }

    /***
     * @description 退出接口
     * @return
     */
    @ApiOperation(value = "退出登录",notes = "退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseResult<Boolean> logout() {
        UserVO userVO = SubjectContent.getUserVO();
        Boolean flag = logoutHandler.logout(userVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED, flag);
    }

}
