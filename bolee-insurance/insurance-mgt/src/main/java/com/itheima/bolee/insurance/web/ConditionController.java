package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.ConditionVO;
import com.itheima.bolee.insurance.service.IConditionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：筛选项响应接口
 */
@Slf4j
@Api(tags = "筛选项")
@RestController
@RequestMapping("condition")
public class ConditionController {

    @Autowired
    IConditionService conditionService;

    /***
     * @description 多条件查询筛选项分页
     * @param conditionVO 筛选项VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<ConditionVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "筛选项分页",notes = "筛选项分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "conditionVO",value = "筛选项VO对象",required = true,dataType = "ConditionVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"conditionVO.dataState","conditionVO.conditionKey","conditionVO.conditionKeyName","conditionVO.conditionVal","conditionVO.sortNo","conditionVO.remake"})
    public ResponseResult<Page<ConditionVO>> findConditionVOPage(
                                    @RequestBody ConditionVO conditionVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {

        return null;
    }

    /**
     * @Description 保存筛选项
     * @param conditionVO 筛选项VO对象
     * @return ConditionVO
     */
    @PutMapping
    @ApiOperation(value = "保存Condition",notes = "添加Condition")
    @ApiImplicitParam(name = "conditionVO",value = "筛选项VO对象",required = true,dataType = "ConditionVO")
    @ApiOperationSupport(includeParameters = {"conditionVO.dataState","conditionVO.conditionKey","conditionVO.conditionKeyName","conditionVO.conditionVal","conditionVO.sortNo","conditionVO.remake"})
    public ResponseResult<ConditionVO> createCondition(@RequestBody ConditionVO conditionVO) {

        return null;
    }

    /**
     * @Description 修改筛选项
     * @param conditionVO 筛选项VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改筛选项",notes = "修改筛选项")
    @ApiImplicitParam(name = "conditionVO",value = "筛选项VO对象",required = true,dataType = "ConditionVO")
    @ApiOperationSupport(includeParameters = {"conditionVO.id","conditionVO.dataState","conditionVO.conditionKey","conditionVO.conditionKeyName","conditionVO.conditionVal","conditionVO.sortNo","conditionVO.remake"})
    public ResponseResult<Boolean> updateCondition(@RequestBody ConditionVO conditionVO) {

        return null;
    }

    /**
     * @Description 删除筛选项
     * @param conditionVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除筛选项",notes = "删除筛选项")
    @ApiImplicitParam(name = "conditionVO",value = "筛选项VO对象",required = true,dataType = "ConditionVO")
    @ApiOperationSupport(includeParameters = {"conditionVO.checkedIds"})
    public ResponseResult<Boolean> deleteCondition(@RequestBody ConditionVO conditionVO) {

        return null;
    }

    /***
     * @description 多条件查询筛选项列表
     * @param conditionVO 筛选项VO对象
     * @return List<ConditionVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询筛选项列表",notes = "多条件查询筛选项列表")
    @ApiImplicitParam(name = "conditionVO",value = "筛选项VO对象",required = true,dataType = "ConditionVO")
    @ApiOperationSupport(includeParameters = {"conditionVO.dataState","conditionVO.conditionKey","conditionVO.conditionKeyName","conditionVO.conditionVal","conditionVO.sortNo","conditionVO.remake"})
    public ResponseResult<List<ConditionVO>> conditionList(@RequestBody ConditionVO conditionVO) {

        return null;
    }

}
