package com.itheima.bolee.trade.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.trade.face.PayChannelFace;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PayChannelController.java
 * @Description 支付通道
 */
@RestController
@RequestMapping("pay-channel")
@Slf4j
@Api(tags = "支付通道")
public class PayChannelController {

    @Autowired
    PayChannelFace payChannelFace;

    /**
     * @Description 支付通道列表
     * @param payChannelVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询支付通道分页",notes = "查询支付通道分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "payChannelVO",value = "支付通道查询对象",required = true,dataType = "PayChannelVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<PayChannelVO>> findPayChannelVOPage(
        @RequestBody PayChannelVO payChannelVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<PayChannelVO> payChannelVOPage = payChannelFace.findPayChannelVOPage(payChannelVO, pageNum, pageSize);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,payChannelVOPage);
    }

    /**
     * @Description 添加支付通道
     * @param payChannelVO 对象信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加支付通道",notes = "添加支付通道")
    @ApiImplicitParam(name = "payChannelVO",value = "支付通道对象",required = true,dataType = "PayChannelVO")
    ResponseResult<PayChannelVO> createPayChannel(@RequestBody PayChannelVO payChannelVO) {
        PayChannelVO payChannelVOResult = payChannelFace.createPayChannel(payChannelVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,payChannelVOResult);
    }

    /**
     * @Description 修改支付通道
     * @param payChannelVO 对象信息
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "修改支付通道",notes = "修改支付通道")
    @ApiImplicitParam(name = "payChannelVO",value = "支付通道对象",required = true,dataType = "PayChannelVO")
    ResponseResult<Boolean> updatePayChannel(@RequestBody PayChannelVO payChannelVO) {
        Boolean flag = payChannelFace.updatePayChannel(payChannelVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,flag);
    }


    @PatchMapping("dataState")
    @ApiOperation(value = "修改支付通道状态",notes = "修改支付通道状态")
    @ApiImplicitParam(name = "payChannelVO",value = "支付通道查询对象",required = true,dataType = "PayChannelVO")
    ResponseResult<Boolean> updatePayChannelEnableFlag(@RequestBody PayChannelVO payChannelVO) {
        Boolean flag = payChannelFace.updatePayChannel(payChannelVO);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,flag);
    }
}
