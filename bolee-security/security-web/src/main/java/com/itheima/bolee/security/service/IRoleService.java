package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.security.pojo.Role;

import java.util.List;

/**
 * @Description：角色表服务类
 */
public interface IRoleService extends IService<Role> {

    /**
     * @Description 多条件查询角色表分页列表
     * @param roleVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<ResourceVO>
     */
    Page<RoleVO> findRolePage(RoleVO roleVO, int pageNum, int pageSize);

    /**
     * @Description 创建角色表
     * @param roleVO 对象信息
     * @return ResourceVO
     */
    RoleVO createRole(RoleVO roleVO);

    /**
     * @Description 修改角色表
     * @param roleVO 对象信息
     * @return Boolean
     */
    Boolean updateRole(RoleVO roleVO);

    /**
     * @description 多条件查询角色表列表
     * @param roleVO 查询条件
     * @return: List<ResourceVO>
     */
    List<RoleVO> findRoleList(RoleVO roleVO);

    /***
     * @description 员工们对应角色
     * @param userIds
     * @return
     */
    List<RoleVO> findRoleVOListInUserId(List<Long> userIds);

    /**
     * @description 员工对应角色
     * @param userId 查询条件
     * @return: List<Role>
     */
    List<RoleVO> findRoleVOListByUserId(Long userId);

}
