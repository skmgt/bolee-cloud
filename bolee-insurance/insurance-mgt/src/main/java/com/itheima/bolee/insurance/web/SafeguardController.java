package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.SafeguardVO;
import com.itheima.bolee.insurance.service.ISafeguardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保障项响应接口
 */
@Slf4j
@Api(tags = "保障项")
@RestController
@RequestMapping("safeguard")
public class SafeguardController {

    @Autowired
    ISafeguardService safeguardService;

    /***
     * @description 多条件查询保障项分页
     * @param safeguardVO 保障项VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<SafeguardVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保障项分页",notes = "保障项分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "safeguardVO",value = "保障项VO对象",required = true,dataType = "SafeguardVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"safeguardVO.dataState","safeguardVO.safeguardKey","safeguardVO.safeguardKeyName","safeguardVO.safeguardVal","safeguardVO.safeguardType","safeguardVO.sortNo","safeguardVO.remake"})
    public ResponseResult<Page<SafeguardVO>> findSafeguardVOPage(
                                    @RequestBody SafeguardVO safeguardVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {

        return null;
    }

    /**
     * @Description 保存保障项
     * @param safeguardVO 保障项VO对象
     * @return SafeguardVO
     */
    @PutMapping
    @ApiOperation(value = "保存Safeguard",notes = "添加Safeguard")
    @ApiImplicitParam(name = "safeguardVO",value = "保障项VO对象",required = true,dataType = "SafeguardVO")
    @ApiOperationSupport(includeParameters = {"safeguardVO.dataState","safeguardVO.safeguardKey","safeguardVO.safeguardKeyName","safeguardVO.safeguardVal","safeguardVO.safeguardType","safeguardVO.sortNo","safeguardVO.remake"})
    public ResponseResult<SafeguardVO> createSafeguard(@RequestBody SafeguardVO safeguardVO) {

        return null;
    }

    /**
     * @Description 修改保障项
     * @param safeguardVO 保障项VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保障项",notes = "修改保障项")
    @ApiImplicitParam(name = "safeguardVO",value = "保障项VO对象",required = true,dataType = "SafeguardVO")
    @ApiOperationSupport(includeParameters = {"safeguardVO.id","safeguardVO.dataState","safeguardVO.safeguardKey","safeguardVO.safeguardKeyName","safeguardVO.safeguardVal","safeguardVO.safeguardType","safeguardVO.sortNo","safeguardVO.remake"})
    public ResponseResult<Boolean> updateSafeguard(@RequestBody SafeguardVO safeguardVO) {

        return null;
    }

    /***
     * @description 多条件查询保障项列表
     * @param safeguardVO 保障项VO对象
     * @return List<SafeguardVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保障项列表",notes = "多条件查询保障项列表")
    @ApiImplicitParam(name = "safeguardVO",value = "保障项VO对象",required = true,dataType = "SafeguardVO")
    @ApiOperationSupport(includeParameters = {"safeguardVO.dataState","safeguardVO.safeguardKey","safeguardVO.safeguardKeyName","safeguardVO.safeguardVal","safeguardVO.safeguardType","safeguardVO.sortNo","safeguardVO.remake"})
    public ResponseResult<List<SafeguardVO>> safeguardList(@RequestBody SafeguardVO safeguardVO) {

        return null;
    }


    /**
     * @Description 删除保障项
     * @param safeguardVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除保障项",notes = "删除保障项")
    @ApiImplicitParam(name = "safeguardVO",value = "保障项VO对象",required = true,dataType = "SafeguardVO")
    @ApiOperationSupport(includeParameters = {"safeguardVO.checkedIds"})
    public ResponseResult<Boolean> deleteSafeguard(@RequestBody SafeguardVO safeguardVO) {

        return null;
    }

    /***
     * @description 按safeguardKey查询SafeguardVO
     * @param safeguardKey 保障项key
     * @return SafeguardVO
     */
    @PostMapping("safeguard-key/{safeguardKey}")
    @ApiOperation(value = "保障项key查询SafeguardVO",notes = "保障项key查询SafeguardVO")
    @ApiImplicitParam(paramType = "path",name = "safeguardKey",value = "保障项key",dataType = "String")
    public ResponseResult<SafeguardVO> findBySafeguardKey(@PathVariable("safeguardKey") String safeguardKey) {
        SafeguardVO safeguardVO = safeguardService.findBySafeguardKey(safeguardKey);
        return ResponseResultBuild.successBuild(safeguardVO);
    }

}
