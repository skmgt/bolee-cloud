package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WarrantyEarningsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WarrantyEarningsOrderVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：给付计划订单服务类
 */
public interface IWarrantyEarningsOrderService extends IService<WarrantyEarningsOrder> {

    /**
     * @Description 多条件查询给付计划订单分页
     * @param warrantyEarningsOrderVO 给付计划订单查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyEarningsOrderVO>
     */
    Page<WarrantyEarningsOrderVO> findPage(WarrantyEarningsOrderVO warrantyEarningsOrderVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询给付计划订单
    * @param warrantyEarningsOrderId 合同信息ID
    * @return WarrantyEarningsOrderVO
    */
    WarrantyEarningsOrderVO findById(String warrantyEarningsOrderId);

    /**
     * @Description 给付计划订单新增
     * @param warrantyEarningsOrderVO 给付计划订单查询条件
     * @return WarrantyEarningsOrderVO
     */
    WarrantyEarningsOrderVO save(WarrantyEarningsOrderVO warrantyEarningsOrderVO);

    /**
     * @Description 给付计划订单修改
     * @param warrantyEarningsOrderVO 给付计划订单对象
     * @return WarrantyEarningsOrderVO
     */
    Boolean update(WarrantyEarningsOrderVO warrantyEarningsOrderVO);

    /**
     * @Description 给付计划订单删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询给付计划订单列表
     * @param warrantyEarningsOrderVO 查询条件
     * @return: List<WarrantyEarningsOrderVO>
     */
    List<WarrantyEarningsOrderVO> findList(WarrantyEarningsOrderVO warrantyEarningsOrderVO);
}
