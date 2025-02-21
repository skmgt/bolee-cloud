package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.security.DeptEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CoefficentVO;
import com.itheima.bolee.insurance.dto.CoefficentVO;
import com.itheima.bolee.insurance.service.ICoefficentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：系数项响应接口
 */
@Slf4j
@Api(tags = "系数项")
@RestController
@RequestMapping("coefficent")
public class CoefficentController {

    @Autowired
    ICoefficentService coefficentService;

    /***
     * @description 多条件查询系数项分页
     * @param coefficentVO 系数项VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CoefficentVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "系数项分页",notes = "系数项分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "coefficentVO",value = "系数项VO对象",required = true,dataType = "CoefficentVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"coefficentVO.dataState","coefficentVO.coefficentKey","coefficentVO.coefficentKeyName","coefficentVO.coefficentVal","coefficentVO.sortNo","coefficentVO.remake","coefficentVO.coefficentType"})
    public ResponseResult<Page<CoefficentVO>> findCoefficentVOPage(
                                    @RequestBody CoefficentVO coefficentVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {

        return null;
    }

    /**
     * @Description 保存系数项
     * @param coefficentVO 系数项VO对象
     * @return CoefficentVO
     */
    @PutMapping
    @ApiOperation(value = "保存Coefficent",notes = "添加Coefficent")
    @ApiImplicitParam(name = "coefficentVO",value = "系数项VO对象",required = true,dataType = "CoefficentVO")
    @ApiOperationSupport(includeParameters = {"coefficentVO.dataState","coefficentVO.coefficentKey","coefficentVO.coefficentKeyName","coefficentVO.coefficentVal","coefficentVO.sortNo","coefficentVO.remake","coefficentVO.coefficentType"})
    public ResponseResult<CoefficentVO> createCoefficent(@RequestBody CoefficentVO coefficentVO) {

        return null;
    }

    /**
     * @Description 修改系数项
     * @param coefficentVO 系数项VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改系数项",notes = "修改系数项")
    @ApiImplicitParam(name = "coefficentVO",value = "系数项VO对象",required = true,dataType = "CoefficentVO")
    @ApiOperationSupport(includeParameters = {"coefficentVO.id","coefficentVO.dataState","coefficentVO.coefficentKey","coefficentVO.coefficentKeyName","coefficentVO.coefficentVal","coefficentVO.sortNo","coefficentVO.remake","coefficentVO.coefficentType"})
    public ResponseResult<Boolean> updateCoefficent(@RequestBody CoefficentVO coefficentVO) {

        return null;
    }

    /**
     * @Description 删除系数项
     * @param coefficentVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除系数项",notes = "删除系数项")
    @ApiImplicitParam(name = "coefficentVO",value = "系数项VO对象",required = true,dataType = "CoefficentVO")
    @ApiOperationSupport(includeParameters = {"coefficentVO.checkedIds"})
    public ResponseResult<Boolean> deleteCoefficent(@RequestBody CoefficentVO coefficentVO) {

        return null;
    }

    /***
     * @description 多条件查询系数项列表
     * @param coefficentVO 系数项VO对象
     * @return List<CoefficentVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询系数项列表",notes = "多条件查询系数项列表")
    @ApiImplicitParam(name = "coefficentVO",value = "系数项VO对象",required = true,dataType = "CoefficentVO")
    @ApiOperationSupport(includeParameters = {"coefficentVO.dataState","coefficentVO.coefficentKey","coefficentVO.coefficentKeyName","coefficentVO.coefficentVal","coefficentVO.sortNo","coefficentVO.remake","coefficentVO.coefficentType"})
    public ResponseResult<List<CoefficentVO>> coefficentList(@RequestBody CoefficentVO coefficentVO) {

        return null;
    }

    /***
     * @description 系数项key查询CoefficentVO
     * @param coefficentKey 系数项key
     * @return CoefficentVO
     */
    @PostMapping("coefficent-key/{coefficentKey}")
    @ApiOperation(value = "系数项key查询CoefficentVO",notes = "系数项key查询CoefficentVO")
    @ApiImplicitParam(paramType = "path",name = "coefficentKey",value = "系数项key",dataType = "String")
    public ResponseResult<CoefficentVO> findByCoefficentKey(@PathVariable("coefficentKey") String coefficentKey) {

        return null;
    }
}
