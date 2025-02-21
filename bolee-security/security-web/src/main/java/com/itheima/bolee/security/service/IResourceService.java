package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.security.MenuVO;
import com.itheima.bolee.framework.commons.dto.security.ResourceVO;
import com.itheima.bolee.security.pojo.Resource;

import java.util.List;

/**
 * @Description：权限表服务类
 */
public interface IResourceService extends IService<Resource> {

    /**
     * @Description 多条件查询权限表分页列表
     * @param resourceVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<Resource>
     */
    Page<ResourceVO> findResourcePage(ResourceVO resourceVO, int pageNum, int pageSize);

    /**
     * @Description 创建权限表
     * @param resourceVO 对象信息
     * @return Resource
     */
    ResourceVO createResource(ResourceVO resourceVO);

    /**
     * @Description 修改权限表
     * @param resourceVO 对象信息
     * @return Boolean
     */
    Boolean updateResource(ResourceVO resourceVO);

    /**
     * @description 多条件查询权限表列表
     * @param resourceVO 查询条件
     * @return: List<Resource>
     */
    List<ResourceVO> findResourceList(ResourceVO resourceVO);

    /**
     * @description 资源树形
     * @param parentResourceNo 根节点
     * @param checkedResourceNos 选择节点
     * @return: TreeVO
     */
    TreeVO resourceTreeVO(String parentResourceNo, String[] checkedResourceNos);

    /**
     * @description 角色对应资源
     * @param roleIds 角色s
     * @return: List<Resource>
     */
    List<ResourceVO> findResourceVOListInRoleId(List<Long> roleIds);

    /***
     * @description 查询左侧菜单
     * @param systemCode 系统编号
     * @return 菜单对象
     */
    List<MenuVO> menus(String systemCode);

    /**
     * @description 员工对应资源
     * @param userId 查询条件
     * @return: List<Resource>
     */
    List<ResourceVO> findResourceVOListByUserId(Long userId);

    /**
     * @Description 创建编号
     * @param parentResourceNo 父部门编号
     * @return
     */
    String createResourceNo(String parentResourceNo);

}
