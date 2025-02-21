package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.security.pojo.UserRole;

import java.util.List;

/**
 * @Description：用户角色关联表服务类
 */
public interface IUserRoleService extends IService<UserRole> {

    /***
     * @description 按用户ID删除用户角色中间表
     * @param userId 用户id
     * @return
     */
    boolean deleteUserRoleByUserId(Long userId);

    /***
     * @description 按用户IDS删除用户角色中间表
     * @param userIds 用户id
     * @return
     */
    boolean deleteUserRoleInUserId(List<Long> userIds);

}
