package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.security.pojo.Customer;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;

import java.util.List;

/**
 * @Description：客户服务类
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * @Description 多条件查询客户分页列表
     * @param customerVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<Customer>
     */
    Page<CustomerVO> findCustomerPage(CustomerVO customerVO, int pageNum, int pageSize);

    /**
     * @Description 创建客户
     * @param customerVO 对象信息
     * @return Customer
     */
    CustomerVO createCustomer(CustomerVO customerVO);

    /**
     * @Description 修改客户
     * @param customerVO 对象信息
     * @return Boolean
     */
    Boolean updateCustomer(CustomerVO customerVO);

    /**
     * @description 多条件查询客户列表
     * @param customerVO 查询条件
     * @return: List<Customer>
     */
    List<CustomerVO> findCustomerList(CustomerVO customerVO);

    /***
     * @description 重置密码
     * @param userId
     * @return
     */
    Boolean resetPassword(String userId);

    /***
     * @description 用户名登录查询
     * @param username 用户名
     * @return
     */
    UserVO usernameLogin(String username,String companyNo);

    /***
     * @description 手机号登录查询
     * @param mobile 手机号
     * @return
     */
    UserVO mobileLogin(String mobile,String companyNo);

    /***
     * @description 微信号登录查询
     * @param openId 唯一识别号
     * @return
     */
    UserVO wechatLogin(String openId,String companyNo);

    /***
     * @description 重置密码
     * @param userId
     * @return
     */
    Boolean resetPasswords(String userId);

    /***
     * @description 发送登录验证码
     * @param mobile 手机号码
     * @return
     */
    Boolean sendLoginCode(String mobile);

    /**
     * 修改用户真实姓名
     * @param userId
     * @param name
     * @return
     */
    Boolean updateRealName(String userId, String name);
}
