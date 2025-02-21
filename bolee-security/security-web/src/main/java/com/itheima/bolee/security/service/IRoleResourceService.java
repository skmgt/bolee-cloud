package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.security.pojo.RoleResource;

import java.util.List;

/**
 * @Description：角色资源关联表服务类
 */
public interface IRoleResourceService extends IService<RoleResource> {

    /***
     * @description 按角色ID删除角色资源中间表
     * @param roleId
     * @return
     */
    Boolean deleteRoleResourceByRoleId(Long roleId);

    /***
     * @description 按角色IDS删除角色资源中间表
     * @param roleIds
     * @return
     */
    Boolean deleteRoleResourceInRoleId(List<Long> roleIds);
}
