package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.WarrantyEarningsOrderVO;
import com.itheima.bolee.insurance.service.IWarrantyEarningsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：给付计划订单响应接口
 */
@Slf4j
@Api(tags = "给付计划订单")
@RestController
@RequestMapping("warranty-earnings-order")
public class WarrantyEarningsOrderController {

    @Autowired
    IWarrantyEarningsOrderService warrantyEarningsOrderService;

    /***
     * @description 多条件查询给付计划订单分页
     * @param warrantyEarningsOrderVO 给付计划订单VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<WarrantyEarningsOrderVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "给付计划订单分页",notes = "给付计划订单分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "warrantyEarningsOrderVO",value = "给付计划订单VO对象",required = true,dataType = "WarrantyEarningsOrderVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"warrantyEarningsOrderVO.orderNo","warrantyEarningsOrderVO.premium","warrantyEarningsOrderVO.warrantyNo","warrantyEarningsOrderVO.currentPeriod","warrantyEarningsOrderVO.scheduleTime","warrantyEarningsOrderVO.actualTime","warrantyEarningsOrderVO.orderState","warrantyEarningsOrderVO.applicantName","warrantyEarningsOrderVO.applicantIdentityCard","warrantyEarningsOrderVO.sortNo"})
    public ResponseResult<Page<WarrantyEarningsOrderVO>> findWarrantyEarningsOrderVOPage(
                                    @RequestBody WarrantyEarningsOrderVO warrantyEarningsOrderVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<WarrantyEarningsOrderVO> warrantyEarningsOrderVOPage = warrantyEarningsOrderService.findPage(warrantyEarningsOrderVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(warrantyEarningsOrderVOPage);
    }

    /**
     * @Description 保存给付计划订单
     * @param warrantyEarningsOrderVO 给付计划订单VO对象
     * @return WarrantyEarningsOrderVO
     */
    @PutMapping
    @ApiOperation(value = "保存WarrantyEarningsOrder",notes = "添加WarrantyEarningsOrder")
    @ApiImplicitParam(name = "warrantyEarningsOrderVO",value = "给付计划订单VO对象",required = true,dataType = "WarrantyEarningsOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyEarningsOrderVO.dataState","warrantyEarningsOrderVO.orderNo","warrantyEarningsOrderVO.premium","warrantyEarningsOrderVO.warrantyNo","warrantyEarningsOrderVO.currentPeriod","warrantyEarningsOrderVO.scheduleTime","warrantyEarningsOrderVO.actualTime","warrantyEarningsOrderVO.orderState","warrantyEarningsOrderVO.applicantName","warrantyEarningsOrderVO.applicantIdentityCard","warrantyEarningsOrderVO.sortNo"})
    public ResponseResult<WarrantyEarningsOrderVO> createWarrantyEarningsOrder(@RequestBody WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        WarrantyEarningsOrderVO warrantyEarningsOrderVOResult = warrantyEarningsOrderService.save(warrantyEarningsOrderVO);
        return ResponseResultBuild.successBuild(warrantyEarningsOrderVOResult);
    }

    /**
     * @Description 修改给付计划订单
     * @param warrantyEarningsOrderVO 给付计划订单VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改给付计划订单",notes = "修改给付计划订单")
    @ApiImplicitParam(name = "warrantyEarningsOrderVO",value = "给付计划订单VO对象",required = true,dataType = "WarrantyEarningsOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyEarningsOrderVO.id","warrantyEarningsOrderVO.dataState","warrantyEarningsOrderVO.orderNo","warrantyEarningsOrderVO.premium","warrantyEarningsOrderVO.warrantyNo","warrantyEarningsOrderVO.currentPeriod","warrantyEarningsOrderVO.scheduleTime","warrantyEarningsOrderVO.actualTime","warrantyEarningsOrderVO.orderState","warrantyEarningsOrderVO.applicantName","warrantyEarningsOrderVO.applicantIdentityCard","warrantyEarningsOrderVO.sortNo"})
    public ResponseResult<Boolean> updateWarrantyEarningsOrder(@RequestBody WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        Boolean flag = warrantyEarningsOrderService.update(warrantyEarningsOrderVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除给付计划订单
     * @param warrantyEarningsOrderVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除给付计划订单",notes = "删除给付计划订单")
    @ApiImplicitParam(name = "warrantyEarningsOrderVO",value = "给付计划订单VO对象",required = true,dataType = "WarrantyEarningsOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyEarningsOrderVO.checkedIds"})
    public ResponseResult<Boolean> deleteWarrantyEarningsOrder(@RequestBody WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        Boolean flag = warrantyEarningsOrderService.delete(warrantyEarningsOrderVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询给付计划订单列表
     * @param warrantyEarningsOrderVO 给付计划订单VO对象
     * @return List<WarrantyEarningsOrderVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询给付计划订单列表",notes = "多条件查询给付计划订单列表")
    @ApiImplicitParam(name = "warrantyEarningsOrderVO",value = "给付计划订单VO对象",required = true,dataType = "WarrantyEarningsOrderVO")
    @ApiOperationSupport(includeParameters = {"warrantyEarningsOrderVO.orderNo","warrantyEarningsOrderVO.premium","warrantyEarningsOrderVO.warrantyNo","warrantyEarningsOrderVO.currentPeriod","warrantyEarningsOrderVO.scheduleTime","warrantyEarningsOrderVO.actualTime","warrantyEarningsOrderVO.orderState","warrantyEarningsOrderVO.applicantName","warrantyEarningsOrderVO.applicantIdentityCard","warrantyEarningsOrderVO.sortNo"})
    public ResponseResult<List<WarrantyEarningsOrderVO>> warrantyEarningsOrderList(@RequestBody WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        List<WarrantyEarningsOrderVO> warrantyEarningsOrderVOList = warrantyEarningsOrderService.findList(warrantyEarningsOrderVO);
        return ResponseResultBuild.successBuild(warrantyEarningsOrderVOList);
    }

}
