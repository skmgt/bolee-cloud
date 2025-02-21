package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.WarrantyOrderVO;
import com.itheima.bolee.insurance.service.IWarrantyOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：合同订单响应接口
 */
@Slf4j
@Api(tags = "保费明细")
@RestController
@RequestMapping("warranty-order")
public class WarrantyOrderController {

    @Autowired
    IWarrantyOrderService warrantyOrderService;

    /***
     * @description 多条件查询合同订单分页
     * @param warrantyOrderVO 合同订单VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<WarrantyOrderVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "合同订单分页",notes = "合同订单分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "warrantyOrderVO",value = "合同订单VO对象",required = true,dataType = "WarrantyOrderVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"warrantyOrderVO.orderNo","warrantyOrderVO.warrantyNo","warrantyOrderVO.periods","warrantyOrderVO.currentPeriod","warrantyOrderVO.premium","warrantyOrderVO.scheduleTime","warrantyOrderVO.actualTime","warrantyOrderVO.graceTime","warrantyOrderVO.revivalTime","warrantyOrderVO.orderState","warrantyOrderVO.sortNo","warrantyOrderVO.companyNo","warrantyOrderVO.tradingChannel"})
    public ResponseResult<Page<WarrantyOrderVO>> findWarrantyOrderVOPage(
                                    @RequestBody WarrantyOrderVO warrantyOrderVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<WarrantyOrderVO> warrantyOrderVOPage = warrantyOrderService.findPage(warrantyOrderVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(warrantyOrderVOPage);
    }

    /**
     * @Description 保存合同订单
     * @param warrantyOrderVO 合同订单VO对象
     * @return WarrantyOrderVO
     */
    @PutMapping
    @ApiOperation(value = "保存WarrantyOrder",notes = "添加WarrantyOrder")
    @ApiImplicitParam(name = "warrantyOrderVO",value = "合同订单VO对象",required = true,dataType = "WarrantyOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyOrderVO.dataState","warrantyOrderVO.orderNo","warrantyOrderVO.warrantyNo","warrantyOrderVO.periods","warrantyOrderVO.currentPeriod","warrantyOrderVO.premium","warrantyOrderVO.scheduleTime","warrantyOrderVO.actualTime","warrantyOrderVO.graceTime","warrantyOrderVO.revivalTime","warrantyOrderVO.orderState","warrantyOrderVO.sortNo","warrantyOrderVO.companyNo","warrantyOrderVO.tradingChannel"})
    public ResponseResult<WarrantyOrderVO> createWarrantyOrder(@RequestBody WarrantyOrderVO warrantyOrderVO) {
        WarrantyOrderVO warrantyOrderVOResult = warrantyOrderService.save(warrantyOrderVO);
        return ResponseResultBuild.successBuild(warrantyOrderVOResult);
    }

    /**
     * @Description 修改合同订单
     * @param warrantyOrderVO 合同订单VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改合同订单",notes = "修改合同订单")
    @ApiImplicitParam(name = "warrantyOrderVO",value = "合同订单VO对象",required = true,dataType = "WarrantyOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyOrderVO.id","warrantyOrderVO.dataState","warrantyOrderVO.orderNo","warrantyOrderVO.warrantyNo","warrantyOrderVO.periods","warrantyOrderVO.currentPeriod","warrantyOrderVO.premium","warrantyOrderVO.scheduleTime","warrantyOrderVO.actualTime","warrantyOrderVO.graceTime","warrantyOrderVO.revivalTime","warrantyOrderVO.orderState","warrantyOrderVO.sortNo","warrantyOrderVO.companyNo","warrantyOrderVO.tradingChannel"})
    public ResponseResult<Boolean> updateWarrantyOrder(@RequestBody WarrantyOrderVO warrantyOrderVO) {
        Boolean flag = warrantyOrderService.update(warrantyOrderVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除合同订单
     * @param warrantyOrderVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除合同订单",notes = "删除合同订单")
    @ApiImplicitParam(name = "warrantyOrderVO",value = "合同订单VO对象",required = true,dataType = "WarrantyOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyOrderVO.checkedIds"})
    public ResponseResult<Boolean> deleteWarrantyOrder(@RequestBody WarrantyOrderVO warrantyOrderVO) {
        Boolean flag = warrantyOrderService.delete(warrantyOrderVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询合同订单列表
     * @param warrantyOrderVO 合同订单VO对象
     * @return List<WarrantyOrderVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询合同订单列表",notes = "多条件查询合同订单列表")
    @ApiImplicitParam(name = "warrantyOrderVO",value = "合同订单VO对象",required = true,dataType = "WarrantyOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyOrderVO.orderNo","warrantyOrderVO.warrantyNo","warrantyOrderVO.periods","warrantyOrderVO.currentPeriod","warrantyOrderVO.premium","warrantyOrderVO.scheduleTime","warrantyOrderVO.actualTime","warrantyOrderVO.graceTime","warrantyOrderVO.revivalTime","warrantyOrderVO.orderState","warrantyOrderVO.sortNo","warrantyOrderVO.companyNo","warrantyOrderVO.tradingChannel"})
    public ResponseResult<List<WarrantyOrderVO>> warrantyOrderList(@RequestBody WarrantyOrderVO warrantyOrderVO) {
        List<WarrantyOrderVO> warrantyOrderVOList = warrantyOrderService.findList(warrantyOrderVO);
        return ResponseResultBuild.successBuild(warrantyOrderVOList);
    }

}
