package com.itheima.bolee.dict.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.dict.service.IDataDictService;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName DataDictController.java
 * @Description 数字字典controller
 */
@Slf4j
@RestController
@RequestMapping("data-dict")
@Api(tags = "数字字典")
public class DataDictController {

    @Autowired
    IDataDictService dataDictService;
    /***
     * @description 数据字典分页
     * @param dataDictVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<DataDictVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "数据字典分页",notes = "数据字典分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dataDictVO",value = "字典查询对象",required = false,dataType = "DataDictVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters ={"dataDictVO.parentKey","","dataDictVO.dataKey",
            "dataDictVO.dataValue","dataDictVO.discription"} )
    ResponseResult<Page<DataDictVO>> findDataDictVOPage(
        @RequestBody DataDictVO dataDictVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        Page<DataDictVO> dataDictVOPage = dataDictService.findDataDictVOPage(dataDictVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(dataDictVOPage);
    }

    /**
     * @Description 保存字典数据
     * @return
     */
    @PostMapping
    @ApiOperation(value = "数字字典添加",notes = "数字字典添加")
    @ApiImplicitParam(name = "dataDictVO",value = "字典信息",required = true,dataType = "DataDictVO")
    @ApiOperationSupport(ignoreParameters ={"dataDictVO.id", "dataDictVO.updateTime",
            "dataDictVO.createBy","dataDictVO.updateBy", "dataDictVO.creator"} )
    public ResponseResult<DataDictVO> saveDataDict(@RequestBody DataDictVO dataDictVO) {
        DataDictVO dataDictVOResult = dataDictService.saveDataDict(dataDictVO);
        return ResponseResultBuild.successBuild(dataDictVOResult);
    }

    /**
     * @Description 修改字典数据
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "数字字典编辑",notes = "数字字典编辑")
    @ApiImplicitParam(name = "dataDictVO",value = "字典信息",required = true,dataType = "DataDictVO")
    @ApiOperationSupport(ignoreParameters ={"dataDictVO.updateTime", "dataDictVO.createBy",
            "dataDictVO.updateBy", "dataDictVO.creator"})
    public ResponseResult<DataDictVO> updateDataDict(@RequestBody DataDictVO dataDictVO) {
        DataDictVO dataDictVOResult = dataDictService.updateDataDict(dataDictVO);
        return ResponseResultBuild.successBuild(dataDictVOResult);
    }

    @PostMapping("data-state")
    @ApiOperation(value = "数字字典状态编辑",notes = "数字字典状态编辑")
    @ApiImplicitParam(name = "dataDictVO",value = "字典信息",required = true,dataType = "DataDictVO")
    @ApiOperationSupport(ignoreParameters ={"dataDictVO.id", "dataDictVO.dataState"})
    ResponseResult<DataDictVO> updateDataDictEnableFlag(@RequestBody DataDictVO dataDictVO) {
        DataDictVO dataDictVOResult = dataDictService.updateDataDict(dataDictVO);
        return ResponseResultBuild.successBuild(dataDictVOResult);
    }

    /**
     * @Description 父项键查询
     * @return List<DataDictVO>
     */
    @PostMapping("parent-key/{parentKey}")
    @ApiOperation(value = "父项键查询",notes = "父项键查询")
    @ApiImplicitParam(paramType = "path",name = "parentKey",value = "字典parentKey",example = "URGE_TYPE",dataType = "String")
    ResponseResult<List<DataDictVO>> findDataDictVOByParentKey(@PathVariable("parentKey") String parentKey) {
        return ResponseResultBuild.successBuild(dataDictService.findDataDictVOByParentKey(parentKey));
    }

    /**
     * @Description 子项键查询
     * @return DataDictVO
     */
    @PostMapping("data-key/{dataKey}")
    @ApiOperation(value = "子项键查询",notes = "子项键查询")
    @ApiImplicitParam(paramType = "path",name = "dataKey",value = "字典dataKey",example = "URGE_TYPE",dataType = "String")
    ResponseResult<DataDictVO> findDataDictVOByDataKey(@PathVariable("dataKey")String dataKey){
        return ResponseResultBuild.successBuild(dataDictService.findDataDictVOByDataKey(dataKey));
    }

}

