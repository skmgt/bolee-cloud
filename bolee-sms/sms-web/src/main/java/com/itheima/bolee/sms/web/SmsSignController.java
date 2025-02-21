package com.itheima.bolee.sms.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.sms.service.ISmsSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SmsSignController.java
 * @Description 短信签名controller
 */
@RestController
@RequestMapping("smsSign")
@Slf4j
@Api(tags = "签名controller")
public class SmsSignController {

    @Autowired
    ISmsSignService smsSignService;

    /**
     * @Description 签名列表
     * @param smsSignVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询签名分页",notes = "查询签名分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "smsSignVO",value = "签名查询对象",required = true,dataType = "SmsSignVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<SmsSignVO>> findSmsSignVOPage(
        @RequestBody SmsSignVO smsSignVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<SmsSignVO> smsSignVOPage = smsSignService.findSmsSignVOPage(smsSignVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(smsSignVOPage);
    }

    /**
     * @Description 查询签名下拉框
     * @return
     */
    @PostMapping("list")
    @ApiOperation(value = "查询签名下拉框",notes = "查询签名下拉框")
    public ResponseResult<List<SmsSignVO>> findSmsSignVOList() {
        List<SmsSignVO> smsSignVOPage = smsSignService.findSmsSignVOList();
        return ResponseResultBuild.successBuild(smsSignVOPage);
    }

    /**
     * @Description 添加签名
     * @param smsSignVO 对象信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加签名",notes = "添加签名")
    @ApiImplicitParam(name = "smsSignVO",value = "签名对象",required = true,dataType = "SmsSignVO")
    ResponseResult<SmsSignVO> createSmsSign(@RequestBody SmsSignVO smsSignVO)  {
        SmsSignVO smsSignVOResult = smsSignService.addSmsSign(smsSignVO);
        return ResponseResultBuild.successBuild(smsSignVOResult);
    }

    /**
     * @Description 修改签名
     * @param smsSignVO 对象信息
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "修改签名",notes = "修改签名")
    @ApiImplicitParam(name = "smsSignVO",value = "签名对象",required = true,dataType = "SmsSignVO")
    ResponseResult<SmsSignVO> updateSmsSign(@RequestBody SmsSignVO smsSignVO)  {
        SmsSignVO smsSignVOResult = smsSignService.modifySmsSign(smsSignVO);
        return ResponseResultBuild.successBuild(smsSignVOResult);
    }

    @PostMapping("disable-enable")
    @ApiOperation(value = "禁用启用",notes = "禁用启用")
    @ApiImplicitParam(name = "smsSignVO",value = "签名查询对象",required = true,dataType = "SmsSignVO")
    ResponseResult<SmsSignVO> disableEnable(@RequestBody SmsSignVO smsSignVO)  {
        SmsSignVO smsSignVoResult = smsSignService.disableEnable(smsSignVO);
        return ResponseResultBuild.successBuild(smsSignVoResult);
    }

}
