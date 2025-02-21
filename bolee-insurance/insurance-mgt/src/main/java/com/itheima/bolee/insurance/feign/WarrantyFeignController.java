package com.itheima.bolee.insurance.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.WarrantyVO;
import com.itheima.bolee.insurance.service.IWarrantyOrderService;
import com.itheima.bolee.insurance.service.IWarrantyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险合同响应接口
 */
@Slf4j
@Api(tags = "保险合同-feign")
@RestController
@RequestMapping("warranty-feign")
public class WarrantyFeignController {

    @Autowired
    IWarrantyService warrantyService;

    @Autowired
    IWarrantyOrderService warrantyOrderService;

    /***
     * @description 系统监听：清理合同保单
     * @param warrantyNo 订单合同号
     * @return: Boolean
     */
    @PostMapping("clean-warranty/{warrantyNo}")
    @ApiOperation(value = "系统监听：清理合同保单",notes = "监听：清理合同保单")
    @ApiImplicitParam(paramType = "path",name = "warrantyNo",value = "保单号",dataType = "String")
    public Boolean cleanWarranty(@PathVariable("warrantyNo") String warrantyNo) {
        Boolean flag = warrantyService.cleanWarranty(warrantyNo);
        return flag;
    }

    /***
     * @description 计划任务:周期代扣
     * @return  扣款结果
     */
    @PostMapping("periodic-pay")
    @ApiOperation(value = "计划任务:周期代扣",notes = "计划任务:周期代扣")
    public Boolean periodicPay() {
        return warrantyOrderService.periodicPay();
    }

    /***
     * @description 系统监听：同步合同支付结果
     * @param orderNo 订单合同号
     * @return: Boolean
     */
    @PostMapping("sync-payment/{orderNo}/{tradeState}")
    @ApiOperation(value = "系统监听：同步合同支付结果",notes = "系统监听：同步合同支付结果")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(paramType = "path",name = "orderNo",value = "合同订单编号",dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "tradeState",value = "交易状态",dataType = "String"),
    })
    public Boolean syncPayment(@PathVariable("orderNo") String orderNo,@PathVariable("tradeState") String tradeState) {
        Boolean flag = warrantyOrderService.syncPayment(orderNo,tradeState);
        return flag;
    }

}
