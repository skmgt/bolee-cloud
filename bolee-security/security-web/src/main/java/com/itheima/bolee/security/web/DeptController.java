package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import com.itheima.bolee.framework.commons.enums.security.DeptEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.IDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：部门前端控制器
 */
@Slf4j
@Api(tags = "部门管理")
@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    IDeptService deptService;

    /***
     * @description 多条件查询部门分页列表
     * @param deptVO 部门Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<DeptVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "部门分页",notes = "部门分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "deptVO",value = "部门Vo对象",required = true,dataType = "DeptVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<DeptVO>> findDeptVOPage(
                                    @RequestBody DeptVO deptVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<DeptVO> deptVOPage = deptService.findDeptPage(deptVO, pageNum, pageSize);
        return ResponseResultBuild.build(DeptEnum.SUCCEED,deptVOPage);
    }

    /**
     * @Description 保存部门
     * @param deptVO 部门Vo对象
     * @return DeptVO
     */
    @PutMapping
    @ApiOperation(value = "部门添加",notes = "部门添加")
    @ApiImplicitParam(name = "deptVO",value = "部门Vo对象",required = true,dataType = "DeptVO")
    @ApiOperationSupport(includeParameters ={"deptVO.parentDeptNo",
          "deptVO.deptName","deptVO.sortNo","deptVO.updateBy","deptVO.createBy"} )
    public ResponseResult<DeptVO> createDept(@RequestBody DeptVO deptVO) {
        DeptVO deptVOResult = deptService.createDept(deptVO);
        return ResponseResultBuild.build(DeptEnum.SUCCEED,deptVOResult);
    }

    /**
     * @Description 修改部门
     * @param deptVO 部门Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "部门修改",notes = "部门修改")
    @ApiImplicitParam(name = "deptVO",value = "部门Vo对象",required = true,dataType = "DeptVO")
    public ResponseResult<Boolean> updateDept(@RequestBody DeptVO deptVO) {
        Boolean flag = deptService.updateDept(deptVO);
        return ResponseResultBuild.build(DeptEnum.SUCCEED,flag);
    }


    /***
     * @description 多条件查询部门列表
     * @param deptVO 部门Vo对象
     * @return List<DeptVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "部门列表",notes = "部门列表")
    @ApiImplicitParam(name = "deptVO",value = "部门Vo对象",required = true,dataType = "DeptVO")
    public ResponseResult<List<DeptVO>> deptList(@RequestBody DeptVO deptVO) {
        List<DeptVO> deptVOList = deptService.findDeptList(deptVO);
        return ResponseResultBuild.build(DeptEnum.SUCCEED,deptVOList);
    }

    /**
     * @Description 组织部门树形
     * @param deptVO 组织部门对象
     * @return
     */
    @PostMapping("tree")
    @ApiOperation(value = "部门树形",notes = "部门树形")
    @ApiImplicitParam(name = "deptVO",value = "组织部门对象",required = false,dataType = "DeptVO")
    public ResponseResult<TreeVO> deptTreeVO(@RequestBody DeptVO deptVO) {
        TreeVO treeVO = deptService.deptTreeVO(deptVO.getParentDeptNo(),deptVO.getCheckedDeptNos());
        return ResponseResultBuild.build(DeptEnum.SUCCEED,treeVO);
    }

}
