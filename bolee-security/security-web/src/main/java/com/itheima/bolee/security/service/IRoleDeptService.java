package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.security.pojo.RoleDept;

import java.util.List;

/**
 * @ClassName IRoleDeptService.java
 * @Description 角色部门关联表
 */
public interface IRoleDeptService extends IService<RoleDept> {


    /***
     * @description 删除角色对应数据权限
     * @param roleId
     * @return
     */
    Boolean deleteRoleDeptByRoleId(Long roleId);

    /***
     * @description 批量删除
     * @param roleIds
     * @return
     */
    Boolean deleteRoleDeptInRoleId(List<Long> roleIds);


}
