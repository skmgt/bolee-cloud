package com.itheima.bolee.security.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.IAuthChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AuthChannelController.java
 * @Description 三方通道配置
 */
@RestController
@RequestMapping("auth-channel")
@Slf4j
@Api(tags = "三方通道配置")
public class AuthChannelController {

    @Autowired
    IAuthChannelService authChannelService;

    /**
     * @Description 三方通道配置列表
     * @param authChannelVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询三方通道配置分页",notes = "查询三方通道配置分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authChannelVO",value = "三方通道配置查询对象",required = true,dataType = "AuthChannelVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<AuthChannelVO>> findAuthChannelVOPage(
        @RequestBody AuthChannelVO authChannelVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<AuthChannelVO> authChannelVOPage = authChannelService.findAuthChannelPage(authChannelVO, pageNum, pageSize);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,authChannelVOPage);
    }

    /**
     * @Description 添加三方通道配置
     * @param authChannelVO 对象信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加三方通道配置",notes = "添加三方通道配置")
    @ApiImplicitParam(name = "authChannelVO",value = "三方通道配置对象",required = true,dataType = "AuthChannelVO")
    ResponseResult<AuthChannelVO> createAuthChannel(@RequestBody AuthChannelVO authChannelVO) {
        AuthChannelVO authChannelVOResult = authChannelService.createAuthChannel(authChannelVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,authChannelVOResult);
    }

    /**
     * @Description 修改三方通道配置
     * @param authChannelVO 对象信息
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "修改三方通道配置",notes = "修改三方通道配置")
    @ApiImplicitParam(name = "authChannelVO",value = "三方通道配置对象",required = true,dataType = "AuthChannelVO")
    ResponseResult<Boolean> updateAuthChannel(@RequestBody AuthChannelVO authChannelVO) {
        Boolean flag = authChannelService.updateAuthChannel(authChannelVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,flag);
    }

}
