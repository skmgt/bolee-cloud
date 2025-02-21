package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.SaleTopInsurance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.SaleTopInsuranceVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：服务类
 */
public interface ISaleTopInsuranceService extends IService<SaleTopInsurance> {

    /**
     * @Description 多条件查询分页
     * @param saleTopInsuranceVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SaleTopInsuranceVO>
     */
    Page<SaleTopInsuranceVO> findPage(SaleTopInsuranceVO saleTopInsuranceVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询
    * @param saleTopInsuranceId 合同信息ID
    * @return SaleTopInsuranceVO
    */
    SaleTopInsuranceVO findById(String saleTopInsuranceId);

    /**
     * @Description 新增
     * @param saleTopInsuranceVO 查询条件
     * @return SaleTopInsuranceVO
     */
    SaleTopInsuranceVO save(SaleTopInsuranceVO saleTopInsuranceVO);

    /**
     * @Description 修改
     * @param saleTopInsuranceVO 对象
     * @return SaleTopInsuranceVO
     */
    Boolean update(SaleTopInsuranceVO saleTopInsuranceVO);

    /**
     * @Description 删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询列表
     * @param saleTopInsuranceVO 查询条件
     * @return: List<SaleTopInsuranceVO>
     */
    List<SaleTopInsuranceVO> findList(SaleTopInsuranceVO saleTopInsuranceVO);
}
