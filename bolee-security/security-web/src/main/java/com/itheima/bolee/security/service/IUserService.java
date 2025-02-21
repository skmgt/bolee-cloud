package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.security.pojo.User;

import java.util.List;

/**
 * @Description：用户表服务类
 */
public interface IUserService extends IService<User> {

    /**
     * @Description 多条件查询用户表分页列表
     * @param userVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<User>
     */
    Page<UserVO> findUserPage(UserVO userVO, int pageNum, int pageSize);

    /**
     * @Description 创建用户表
     * @param userVO 对象信息
     * @return User
     */
    UserVO createUser(UserVO userVO);

    /**
     * @Description 修改用户表
     * @param userVO 对象信息
     * @return Boolean
     */
    Boolean updateUser(UserVO userVO);

    /**
     * @description 多条件查询用户表列表
     * @param userVO 查询条件
     * @return: List<User>
     */
    List<UserVO> findUserList(UserVO userVO);

    /**
     * @description 部门下员工
     * @param deptNo 部门
     * @return: List<User>
     */
    List<UserVO> findUserVOListByDeptNo(String deptNo);

    /**
     * @description 角色下员工
     * @param roleId 角色
     * @return: List<User>
     */
    List<UserVO> findUserVOListByRoleId(Long roleId);

    /***
     * @description 重置密码
     * @param userId
     * @return
     */
    Boolean resetPasswords(String userId);

    /***
     * @description 查询用户数据权限
     * @param roleVOList 角色列表
     * @param userId 用户
     * @return
     */
    DataSecurityVO userDataSecurity(List<RoleVO> roleVOList, Long userId);

    /***
     * @description 用户名登录查询
     * @param username 用户名
     * @return: com.itheima.easy.vo.UserVO
     */
    UserVO usernameLogin(String username,String companyNo);

    /***
     * @description 手机号登录查询
     * @param mobile 手机号
     * @return: com.itheima.easy.vo.UserVO
     */
    UserVO mobileLogin(String mobile,String companyNo);

    /***
     * @description 微信号登录查询
     * @param openId 唯一识别号
     * @return: com.itheima.easy.vo.UserVO
     */
    UserVO wechatLogin(String openId,String companyNo);

}
