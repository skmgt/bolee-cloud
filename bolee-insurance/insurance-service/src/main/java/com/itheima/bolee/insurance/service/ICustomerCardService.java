package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import com.itheima.bolee.insurance.pojo.CustomerCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CustomerCardVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：绑卡信息服务类
 */
public interface ICustomerCardService extends IService<CustomerCard> {

    /**
     * @Description 多条件查询绑卡信息分页
     * @param customerCardVO 绑卡信息查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CustomerCardVO>
     */
    Page<CustomerCardVO> findPage(CustomerCardVO customerCardVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询绑卡信息
    * @param customerCardId 合同信息ID
    * @return CustomerCardVO
    */
    CustomerCardVO findById(String customerCardId);

    /**
     * @Description 绑卡信息新增
     * @param customerCardVO 绑卡信息查询条件
     * @return CustomerCardVO
     */
    CustomerCardVO save(CustomerCardVO customerCardVO);

    /**
     * @Description 绑卡信息修改
     * @param customerCardVO 绑卡信息对象
     * @return CustomerCardVO
     */
    Boolean update(CustomerCardVO customerCardVO);

    /**
     * @Description 绑卡信息删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询绑卡信息列表
     * @param customerCardVO 查询条件
     * @return: List<CustomerCardVO>
     */
    List<CustomerCardVO> findList(CustomerCardVO customerCardVO);


    /**
     * @description 多条件查询绑卡信息列表
     * @param customerCardVO 查询条件
     * @return: List<CustomerCardVO>
     */
    List<CustomerCardVO> bankCardList(CustomerCardVO customerCardVO);

    /**
     * 报错银行卡
     * @param customerCardVO
     * @return
     */
    CustomerCardVO saveBankCard(CustomerCardVO customerCardVO);

    /**
     * 默认银行卡设置
     * @param customerCardId
     * @return
     */
    CustomerCardVO defaultBankCard(String customerCardId);

    /**
     * 解绑银行卡
     * @param customerCardVO
     * @return
     */
    CustomerCardVO removeBankCard(CustomerCardVO customerCardVO);
}
