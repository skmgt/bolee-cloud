package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.DeptPostUserVO;
import com.itheima.bolee.security.pojo.DeptPostUser;

import java.util.List;

/**
 * @Description：部门岗位用户关联表服务类
 */
public interface IDeptPostUserService extends IService<DeptPostUser> {

    /**
     * @description 用户集合对应的部门岗位用户关联表列表
     * @param userIds 查询条件
     * @return: List<DeptPostUser>
     */
    List<DeptPostUserVO> findDeptPostUserVOListInUserId(List<Long> userIds);

    /***
     * @description 删除用户的部门职位
     * @param userId 用户id
     * @return
     */
    Boolean deleteDeptPostUserByUserId(Long userId);

    /***
     * @description 删除用户IDS的部门职位
     * @param userIds 用户id
     * @return
     */
    Boolean deleteDeptPostUserInUserId(List<String> userIds);


    /**
     * @description 用户的默认部门
     * @param userId 查询条件
     * @return: List<DeptPostUser>
     */
    DeptPostUserVO findDeptPostUserVOByUserId(Long userId);
}
