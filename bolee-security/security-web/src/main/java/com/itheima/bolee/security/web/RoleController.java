package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.framework.commons.enums.security.RoleEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：角色前端控制器
 */
@Slf4j
@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    IRoleService roleService;

    /***
     * @description 多条件查询角色分页列表
     * @param roleVO 角色Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<RoleVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "角色分页",notes = "角色分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleVO",value = "角色Vo对象",required = true,dataType = "RoleVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<RoleVO>> findRoleVOPage(
                                    @RequestBody RoleVO roleVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<RoleVO> roleVOPage = roleService.findRolePage(roleVO, pageNum, pageSize);
        return ResponseResultBuild.build(RoleEnum.SUCCEED,roleVOPage);
    }

    /**
     * @Description 保存角色
     * @param roleVO 角色Vo对象
     * @return RoleVO
     */
    @PutMapping
    @ApiOperation(value = "角色添加",notes = "角色添加")
    @ApiImplicitParam(name = "roleVO",value = "角色Vo对象",required = true,dataType = "RoleVO")
    public ResponseResult<RoleVO> createRole(@RequestBody RoleVO roleVO) {
        RoleVO roleVOResult = roleService.createRole(roleVO);
        return ResponseResultBuild.build(RoleEnum.SUCCEED,roleVOResult);
    }

    /**
     * @Description 修改角色
     * @param roleVO 角色Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "角色修改",notes = "角色修改")
    @ApiImplicitParam(name = "roleVO",value = "角色Vo对象",required = true,dataType = "RoleVO")
    public ResponseResult<Boolean> updateRole(@RequestBody RoleVO roleVO) {
        Boolean flag = roleService.updateRole(roleVO);
        return ResponseResultBuild.build(RoleEnum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询角色列表
     * @param roleVO 角色Vo对象
     * @return List<RoleVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "角色列表",notes = "角色列表")
    @ApiImplicitParam(name = "roleVO",value = "角色Vo对象",required = true,dataType = "RoleVO")
    public ResponseResult<List<RoleVO>> roleList(@RequestBody RoleVO roleVO) {
        List<RoleVO> roleVOList = roleService.findRoleList(roleVO);
        return ResponseResultBuild.build(RoleEnum.SUCCEED,roleVOList);
    }

}
