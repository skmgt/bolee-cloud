package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.itheima.bolee.insurance.dto.SaleTopInsuranceVO;
import com.itheima.bolee.insurance.service.ICategoryService;
import com.itheima.bolee.insurance.service.ISaleTopInsuranceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：响应接口
 */
@Slf4j
@Api(tags = "热销榜")
@RestController
@RequestMapping("sale-top-insurance")
public class SaleTopInsuranceController {

    @Autowired
    ISaleTopInsuranceService saleTopInsuranceService;

    @Autowired
    ICategoryService categoryService;

    /***
     * @description 多条件查询分页
     * @param saleTopInsuranceVO VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<SaleTopInsuranceVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "分页",notes = "分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "saleTopInsuranceVO",value = "VO对象",required = true,dataType = "SaleTopInsuranceVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"saleTopInsuranceVO.checkRule","saleTopInsuranceVO.insuranceId","saleTopInsuranceVO.insuranceName","saleTopInsuranceVO.insuranceDetail","saleTopInsuranceVO.comment","saleTopInsuranceVO.remake","saleTopInsuranceVO.categoryNo","saleTopInsuranceVO.companyNo"})
    public ResponseResult<Page<SaleTopInsuranceVO>> findSaleTopInsuranceVOPage(
                                    @RequestBody SaleTopInsuranceVO saleTopInsuranceVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<SaleTopInsuranceVO> saleTopInsuranceVOPage = saleTopInsuranceService.findPage(saleTopInsuranceVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(saleTopInsuranceVOPage);
    }

    /**
     * @Description 保存
     * @param saleTopInsuranceVO VO对象
     * @return SaleTopInsuranceVO
     */
    @PutMapping
    @ApiOperation(value = "保存SaleTopInsurance",notes = "添加SaleTopInsurance")
    @ApiImplicitParam(name = "saleTopInsuranceVO",value = "VO对象",required = true,dataType = "SaleTopInsuranceVO")
    @ApiOperationSupport(includeParameters = {"saleTopInsuranceVO.dataState","saleTopInsuranceVO.checkRule","saleTopInsuranceVO.insuranceId","saleTopInsuranceVO.insuranceName","saleTopInsuranceVO.insuranceDetail","saleTopInsuranceVO.comment","saleTopInsuranceVO.remake","saleTopInsuranceVO.categoryNo","saleTopInsuranceVO.companyNo"})
    public ResponseResult<SaleTopInsuranceVO> createSaleTopInsurance(@RequestBody SaleTopInsuranceVO saleTopInsuranceVO) {
        SaleTopInsuranceVO saleTopInsuranceVOResult = saleTopInsuranceService.save(saleTopInsuranceVO);
        return ResponseResultBuild.successBuild(saleTopInsuranceVOResult);
    }

    /**
     * @Description 修改
     * @param saleTopInsuranceVO VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改",notes = "修改")
    @ApiImplicitParam(name = "saleTopInsuranceVO",value = "VO对象",required = true,dataType = "SaleTopInsuranceVO")
    @ApiOperationSupport(includeParameters = {"saleTopInsuranceVO.id","saleTopInsuranceVO.dataState","saleTopInsuranceVO.checkRule","saleTopInsuranceVO.insuranceId","saleTopInsuranceVO.insuranceName","saleTopInsuranceVO.insuranceDetail","saleTopInsuranceVO.comment","saleTopInsuranceVO.remake","saleTopInsuranceVO.categoryNo","saleTopInsuranceVO.companyNo"})
    public ResponseResult<Boolean> updateSaleTopInsurance(@RequestBody SaleTopInsuranceVO saleTopInsuranceVO) {
        Boolean flag = saleTopInsuranceService.update(saleTopInsuranceVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除
     * @param saleTopInsuranceVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除",notes = "删除")
    @ApiImplicitParam(name = "saleTopInsuranceVO",value = "VO对象",required = true,dataType = "SaleTopInsuranceVO")
    @ApiOperationSupport(includeParameters = {"saleTopInsuranceVO.checkedIds"})
    public ResponseResult<Boolean> deleteSaleTopInsurance(@RequestBody SaleTopInsuranceVO saleTopInsuranceVO) {
        Boolean flag = saleTopInsuranceService.delete(saleTopInsuranceVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询列表
     * @param saleTopInsuranceVO VO对象
     * @return List<SaleTopInsuranceVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询列表",notes = "多条件查询列表")
    @ApiImplicitParam(name = "saleTopInsuranceVO",value = "VO对象",required = true,dataType = "SaleTopInsuranceVO")
    @ApiOperationSupport(includeParameters = {"saleTopInsuranceVO.checkRule","saleTopInsuranceVO.insuranceId","saleTopInsuranceVO.insuranceName","saleTopInsuranceVO.insuranceDetail","saleTopInsuranceVO.comment","saleTopInsuranceVO.remake","saleTopInsuranceVO.categoryNo","saleTopInsuranceVO.companyNo"})
    public ResponseResult<List<SaleTopInsuranceVO>> saleTopInsuranceList(@RequestBody SaleTopInsuranceVO saleTopInsuranceVO) {
        List<SaleTopInsuranceVO> saleTopInsuranceVOList = saleTopInsuranceService.findList(saleTopInsuranceVO);
        return ResponseResultBuild.successBuild(saleTopInsuranceVOList);
    }


    /***
     * @description 人种榜/险种榜分类
     * @return List<CategoryVO>
     */
    @PostMapping("category-check-rule/{type}")
    @ApiOperation(value = "人种/险种分类",notes = "人种/险种分类")
    public ResponseResult<List<CategoryVO>> categoryCheckRule(@PathVariable("type")String type) {
        List<CategoryVO> res = categoryService.categoryCheckRule(type);
        return ResponseResultBuild.successBuild(res);
    }
}
