package com.itheima.bolee.sms.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.sms.service.ISmsChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SmsChannelController.java
 * @Description 短信通道controller
 */
@RestController
@RequestMapping("smsChannel")
@Slf4j
@Api(tags = "通道controller")
public class SmsChannelController {

    @Autowired
    ISmsChannelService smsChannelService;

    /**
     * @Description 通道列表
     * @param smsChannelVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询通道分页",notes = "查询通道分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "smsChannelVO",value = "通道查询对象",required = true,dataType = "SmsChannelVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<SmsChannelVO>> findSmsChannelVOPage(
            @RequestBody SmsChannelVO smsChannelVO,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) {
        Page<SmsChannelVO> smsChannelVOPage = smsChannelService.findSmsChannelVOPage(smsChannelVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(smsChannelVOPage);
    }


    /**
     * @Description 添加通道
     * @param smsChannelVO 对象信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加通道",notes = "添加通道")
    @ApiImplicitParam(name = "smsChannelVO",value = "通道对象",required = true,dataType = "SmsChannelVO")
    ResponseResult<SmsChannelVO> createSmsChannel(@RequestBody SmsChannelVO smsChannelVO) {
        SmsChannelVO smsChannelVOResult = smsChannelService.createSmsChannel(smsChannelVO);
        return ResponseResultBuild.successBuild(smsChannelVOResult);
    }

    /**
     * @Description 修改通道
     * @param smsChannelVO 对象信息
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "修改通道",notes = "修改通道")
    @ApiImplicitParam(name = "smsChannelVO",value = "通道对象",required = true,dataType = "SmsChannelVO")
    ResponseResult<SmsChannelVO> updateSmsChannel(@RequestBody SmsChannelVO smsChannelVO) {
        SmsChannelVO smsChannelVOResult = smsChannelService.updateSmsChannel(smsChannelVO);
        return ResponseResultBuild.successBuild(smsChannelVOResult);
    }

    /**
     * @Description 删除通道
     * @param smsChannelVO 查询对象
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除通道",notes = "删除通道")
    @ApiImplicitParam(name = "smsChannelVO",value = "通道查询对象",required = true,dataType = "SmsChannelVO")
    ResponseResult<Boolean> deleteSmsChannel(@RequestBody SmsChannelVO smsChannelVO ) {
        String[] checkedIds = smsChannelVO.getCheckedIds();
        Boolean flag = smsChannelService.deleteSmsChannel(checkedIds);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 修改通道状态
     * @param smsChannelVO 查询对象
     * @return
     */
    @PostMapping("update-smsChannel-enableFlag")
    @ApiOperation(value = "修改通道状态",notes = "修改通道状态")
    @ApiImplicitParam(name = "smsChannelVO",value = "通道查询对象",required = true,dataType = "SmsChannelVO")
    ResponseResult<SmsChannelVO> updateSmsChannelEnableFlag(@RequestBody SmsChannelVO smsChannelVO) {
        SmsChannelVO smsChannelVOResult = smsChannelService.updateSmsChannel(smsChannelVO);
        return ResponseResultBuild.successBuild(smsChannelVOResult);
    }

    /**
     * @Description 查询通道下拉框
     * @return
     */
    @PostMapping("list")
    @ApiOperation(value = "查询通道下拉框",notes = "查询通道下拉框")
    public ResponseResult<List<SmsChannelVO>> findSmsChannelVOList() {
        List<SmsChannelVO> smsChannelVOs = smsChannelService.findSmsChannelVOList();
        return ResponseResultBuild.successBuild(smsChannelVOs);
    }
}
