package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.outinterface.dto.TokenDTO;
import com.itheima.bolee.insurance.constant.CustomerInfoCacheConstant;
import com.itheima.bolee.insurance.dto.BaiduCloudTokenVO;
import com.itheima.bolee.insurance.dto.CustomerCardVO;
import com.itheima.bolee.insurance.pojo.CustomerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description：客户信息表服务类
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * @Description 多条件查询客户信息表分页
     * @param customerInfoVO 客户信息表查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CustomerInfoVO>
     */
    Page<CustomerInfoVO> findPage(CustomerInfoVO customerInfoVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询客户信息表
    * @param customerInfoId 合同信息ID
    * @return CustomerInfoVO
    */
    CustomerInfoVO findById(String customerInfoId);

    /**
     * @Description 客户信息表新增
     * @param customerInfoVO 客户信息表查询条件
     * @return CustomerInfoVO
     */
    CustomerInfoVO save(CustomerInfoVO customerInfoVO);

    /**
     * @Description 客户信息表修改
     * @param customerInfoVO 客户信息表对象
     * @return CustomerInfoVO
     */
    Boolean update(CustomerInfoVO customerInfoVO);

    /**
     * @Description 客户信息表删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询客户信息表列表
     * @param customerInfoVO 查询条件
     * @return: List<CustomerInfoVO>
     */
    List<CustomerInfoVO> findList(CustomerInfoVO customerInfoVO);


    /**
     * 获取百度云的ocrtoken
     * @param userVO
     * @return
     */
    BaiduCloudTokenVO getOcrToken(UserVO userVO);

    /**
     * 重置密码
     * @return
     */
    Boolean resetPassword(UserVO userVO);

    /***
     * @description 按身份证查询投保人信息
     * @param identityCard
     * @return
 * @return: com.itheima.bolee.insurance.dto.CustomerInfoVO
     */
    CustomerInfoVO findOneByIdentityCard(String identityCard);

    /**
     * 人脸识别认证
     * @return
     */
    Boolean faceCheck();

    /**
     * 身份证号
     * @param customerInfo
     * @return
     */
    CustomerInfoVO idCard(CustomerInfoVO customerInfo);

    /**
     * 获取身份证信息
     * @return
     */
    CustomerInfoVO idCardInfo();

}
