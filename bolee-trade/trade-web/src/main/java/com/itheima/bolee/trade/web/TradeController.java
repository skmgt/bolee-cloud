package com.itheima.bolee.trade.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.trade.face.TradeFace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName TradeFace.java
 * @Description 交易Face接口
 */
@RequestMapping("trade")
@RestController
@Api(tags = "账单明细")
public class TradeController {

    @Autowired
    TradeFace tradeFace;

    /**
     * @Description 账单明细
     * @param tradeVO 查询条件
     * @return
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "账单明细",notes = "账单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradeVO",value = "支付通道查询对象",required = true,dataType = "TradeVO"),
            @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",dataType = "Integer"),
            @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",dataType = "Integer")
    })
    public ResponseResult<Page<TradeVO>> findTradeVOPage(
            @RequestBody TradeVO tradeVO,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) {
        Page<TradeVO> page = tradeFace.findTradeVOPage(tradeVO, pageNum, pageSize);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,page);
    }
}
