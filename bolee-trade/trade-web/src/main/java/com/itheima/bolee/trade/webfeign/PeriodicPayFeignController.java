package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.trade.face.PeriodicPayFace;
import com.itheima.bolee.trade.service.ISignContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PeriodicPayFeignController.java
 * @Description 周期性扣款
 */
@RequestMapping("trade-periodic-feign")
@RestController
@Api(tags = "周期性扣款feign-controller")
public class PeriodicPayFeignController {

    @Autowired
    PeriodicPayFace periodicPayFace;

    @Autowired
    ISignContractService signContractService;

    /***
     * @description App支付并签约
     * @param tradeVO 交易单信息
     * @return
     */
    @PostMapping("app-pay-sign")
    @ApiOperation(value = "App支付并签约",notes = "App支付并签约")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    TradeVO appPaySign(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = periodicPayFace.appPaySign(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description H5先签约
     * @param tradeVO 交易单信息
     * @return
     */
    @PostMapping("h5-sign-contract")
    @ApiOperation(value = "H5先签约",notes = "H5先签约")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    TradeVO h5SignContract(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = periodicPayFace.h5SignContract(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description App支付并签约
     * @param tradeVO 交易单信息
     * @return
     */
    @PostMapping("h5-periodic-pay")
    @ApiOperation(value = "5签约后代扣",notes = "5签约后代扣")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    TradeVO h5PeriodicPay(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = periodicPayFace.h5PeriodicPay(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description 签约同步
     * @param warrantyNo 合同号
     * @param tradingChannel 交易渠道
     * @param agreementNo 签约号
     * @return
     */
    @PostMapping("sign-contract-sync/{warrantyNo}/{tradingChannel}/{agreementNo}")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyNo",value = "合同号",dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "渠道号",dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "agreementNo",value = "签约号",dataType = "String")
    })
    Boolean signContractSync(@PathVariable("warrantyNo") String warrantyNo,
                            @PathVariable("tradingChannel")String tradingChannel,
                            @PathVariable("agreementNo")String agreementNo){
        return signContractService.signContractSync(warrantyNo,tradingChannel,agreementNo);
    }

    /***
     * @description 关闭h5签约
     * @param warrantyNo 合同号
     * @return
     */
    @PostMapping("h5-close-sign-contract/{warrantyNo}/{tradingChannel}")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyNo",value = "合同号",dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "渠道号",dataType = "String")
    })
    Boolean h5CloseSignContract(@PathVariable("warrantyNo") String warrantyNo,
                        @PathVariable("tradingChannel")String tradingChannel){
        return signContractService.h5CloseSignContract(warrantyNo,tradingChannel);
    }
}
