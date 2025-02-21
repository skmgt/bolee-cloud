package com.itheima.bolee.sms.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SmsTemplateController.java
 * @Description 短信模板controller
 */
@RestController
@RequestMapping("smsTemplate")
@Slf4j
@Api(tags = "模板controller")
public class SmsTemplateController {

    @Autowired
    ISmsTemplateService smsTemplateService;

    /**
     * @Description 模板列表
     * @param smsTemplateVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询模板分页",notes = "查询模板分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "smsTemplateVO",value = "模板查询对象",required = true,dataType = "SmsTemplateVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<SmsTemplateVO>> findSmsTemplateVOPage(
        @RequestBody SmsTemplateVO smsTemplateVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<SmsTemplateVO> smsTemplateVOPage = smsTemplateService.findSmsTemplateVOPage(smsTemplateVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(smsTemplateVOPage);
    }

    /**
     * @Description 添加模板
     * @param smsTemplateVO 对象信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加模板",notes = "添加模板")
    @ApiImplicitParam(name = "smsTemplateVO",value = "模板对象",required = true,dataType = "SmsTemplateVO")
    ResponseResult<SmsTemplateVO> createSmsTemplate(@RequestBody SmsTemplateVO smsTemplateVO) {
        SmsTemplateVO smsTemplateVOResult = smsTemplateService.addSmsTemplate(smsTemplateVO);
        return ResponseResultBuild.successBuild(smsTemplateVOResult);
    }

    /**
     * @Description 修改模板
     * @param smsTemplateVO 对象信息
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "修改模板",notes = "修改模板")
    @ApiImplicitParam(name = "smsTemplateVO",value = "模板对象",required = true,dataType = "SmsTemplateVO")
    ResponseResult<SmsTemplateVO> updateSmsTemplate(@RequestBody SmsTemplateVO smsTemplateVO) {
        SmsTemplateVO smsTemplateVOResult = smsTemplateService.modifySmsTemplate(smsTemplateVO);
        return ResponseResultBuild.successBuild(smsTemplateVOResult);
    }

    @PostMapping("disable-enable")
    @ApiOperation(value = "禁用启用",notes = "禁用启用")
    @ApiImplicitParam(name = "smsTemplateVO",value = "模板查询对象",required = true,dataType = "SmsTemplateVO")
    ResponseResult<SmsTemplateVO> disableEnable(@RequestBody SmsTemplateVO smsTemplateVO) {
        SmsTemplateVO smsTemplateVOResult = smsTemplateService.disableEnable(smsTemplateVO);
        return ResponseResultBuild.successBuild(smsTemplateVOResult);
    }

}
