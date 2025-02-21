package com.itheima.bolee.sms.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SmsSendRecordController.java
 * @Description 发送记录Controller
 */
@RestController
@RequestMapping("sms-send-record")
@Slf4j
@Api(tags = "发送记录controller")
public class SmsSendRecordController {

    @Autowired
    ISmsSendRecordService smsSendRecordService;

    /**
     * @Description 发送记录列表
     * @param smsSendRecordVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询发送记录分页",notes = "查询发送记录分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "smsSendRecordVO",value = "发送记录查询对象",required = true,dataType = "SmsSendRecordVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<SmsSendRecordVO>> findSmsSendRecordVOPage(
        @RequestBody SmsSendRecordVO smsSendRecordVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<SmsSendRecordVO> smsSendRecordVOPage =
                smsSendRecordService.findSmsSendRecordVOPage(smsSendRecordVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(smsSendRecordVOPage);
    }

    /**
     * @Description 查找发送记录
     * @param smsSendRecordId 登录名
     * @return
     */
    @PostMapping("{smsSendRecordId}")
    @ApiOperation(value = "查找发送记录",notes = "查找发送记录")
    @ApiImplicitParam(paramType = "path",name = "smsSendRecordId",value = "发送记录Id",dataType = "Long")
    ResponseResult<SmsSendRecordVO> findSmsSendRecordBySmsSendRecordId(@PathVariable("smsSendRecordId") Long smsSendRecordId) {
        SmsSendRecordVO smsSendRecordVO = smsSendRecordService.findSmsSendRecordBySmsSendRecordId(smsSendRecordId);
        return ResponseResultBuild.successBuild(smsSendRecordVO);
    }

    @PostMapping("retrySendSms")
    @ApiOperation(value = "重发",notes = "重发")
    @ApiImplicitParam(name = "smsSendRecordVO",value = "发送记录查询对象",required = true,dataType = "SmsSendRecordVO")
    ResponseResult<Boolean> retrySendSms(@RequestBody SmsSendRecordVO smsSendRecordVO) {
        Boolean flag = smsSendRecordService.retrySendSms(smsSendRecordVO);
        return ResponseResultBuild.successBuild(flag);
    }

}
