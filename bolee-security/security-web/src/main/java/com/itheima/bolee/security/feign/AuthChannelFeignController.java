package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.dto.security.*;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.security.service.IAuthChannelService;
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
 * @ClassName AuthChannelFeignController.java
 * @Description 授权三方表Mapper接口
 */
@RestController
@RequestMapping("auth-channel-feign")
@Api(tags = "用户feign管理")
@Slf4j
public class AuthChannelFeignController {

    @Autowired
    IAuthChannelService authChannelService;

    /**
     * @Description 按企业编号和配置类型查询对应的企业信息
     * @param companyNo 企业编号
     * @return
     */
    @ApiOperation(value = "企业配置",notes = "企业配置")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "companyNo",value = "企业编号",dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "channelLabel",value = "配置标识",dataType = "String")
    })
    @PostMapping("find-auth-channel/{companyNo}/{channelLabel}")
    public AuthChannelVO findAuthChannelByCompanyNoAndChannelLabel(@PathVariable("companyNo") String companyNo,
               @PathVariable("channelLabel") String channelLabel){
        return authChannelService.findAuthChannelByCompanyNoAndChannelLabel(companyNo,channelLabel);
    }
}
