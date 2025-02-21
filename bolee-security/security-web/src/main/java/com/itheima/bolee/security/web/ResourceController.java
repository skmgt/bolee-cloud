package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.security.MenuVO;
import com.itheima.bolee.framework.commons.dto.security.ResourceVO;
import com.itheima.bolee.framework.commons.enums.security.DeptEnum;
import com.itheima.bolee.framework.commons.enums.security.ResourceEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：资源前端控制器
 */
@Slf4j
@Api(tags = "资源管理")
@RestController
@RequestMapping("resource")
public class ResourceController {

    @Autowired
    IResourceService resourceService;

    /***
     * @description 多条件资源分页查询
     * @param resourceVO 资源Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<ResourceVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "资源分页",notes = "资源分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "resourceVO",value = "资源Vo对象",required = true,dataType = "ResourceVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<ResourceVO>> findResourceVOPage(
                                    @RequestBody ResourceVO resourceVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<ResourceVO> resourceVOPage = resourceService.findResourcePage(resourceVO, pageNum, pageSize);
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,resourceVOPage);
    }

    /**
     * @Description 保存资源
     * @param resourceVO 资源Vo对象
     * @return ResourceVO
     */
    @PutMapping
    @ApiOperation(value = "资源添加",notes = "资源添加")
    @ApiImplicitParam(name = "resourceVO",value = "资源Vo对象",required = true,dataType = "ResourceVO")
    public ResponseResult<ResourceVO> createResource(@RequestBody ResourceVO resourceVO) {
        ResourceVO resourceVOResult = resourceService.createResource(resourceVO);
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,resourceVOResult);
    }

    /**
     * @Description 修改资源
     * @param resourceVO 资源Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "资源修改",notes = "资源修改")
    @ApiImplicitParam(name = "resourceVO",value = "资源Vo对象",required = true,dataType = "ResourceVO")
    public ResponseResult<Boolean> updateResource(@RequestBody ResourceVO resourceVO) {
        Boolean flag = resourceService.updateResource(resourceVO);
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询资源列表
     * @param resourceVO 资源Vo对象
     * @return List<ResourceVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "资源列表",notes = "资源列表")
    @ApiImplicitParam(name = "resourceVO",value = "资源Vo对象",required = true,dataType = "ResourceVO")
    public ResponseResult<List<ResourceVO>> resourceList(@RequestBody ResourceVO resourceVO) {
        List<ResourceVO> resourceVOList = resourceService.findResourceList(resourceVO);
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,resourceVOList);
    }

    /**
     * @Description 资源树形
     * @param resourceVO 资源对象
     * @return
     */
    @PostMapping("tree")
    @ApiOperation(value = "资源树形",notes = "资源树形")
    @ApiImplicitParam(name = "resourceVO",value = "资源对象",required = false,dataType = "ResourceVO")
    public ResponseResult<TreeVO> resourceTreeVO(@RequestBody ResourceVO resourceVO) {
        TreeVO treeVO = resourceService.resourceTreeVO(resourceVO.getParentResourceNo(), resourceVO.getCheckedResourceNos());
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,treeVO);
    }

    /**
     * @Description 左侧菜单
     * @return
     */
    @PostMapping("menus/{systemCode}")
    @ApiOperation(value = "左侧菜单",notes = "左侧菜单")
    @ApiImplicitParam(name = "systemCode",value = "系统code",required = false,dataType = "systemCode")
    public ResponseResult<List<MenuVO>> menus(@PathVariable("systemCode") String systemCode) {
        List<MenuVO> menus = resourceService.menus(systemCode);
        return ResponseResultBuild.build(ResourceEnum.SUCCEED,menus);
    }

}