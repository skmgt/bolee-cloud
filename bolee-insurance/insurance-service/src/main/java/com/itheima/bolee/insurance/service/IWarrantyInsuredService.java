package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WarrantyInsured;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WarrantyInsuredVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：合同被保人服务类
 */
public interface IWarrantyInsuredService extends IService<WarrantyInsured> {

    /**
     * @Description 多条件查询合同被保人分页
     * @param warrantyInsuredVO 合同被保人查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyInsuredVO>
     */
    Page<WarrantyInsuredVO> findPage(WarrantyInsuredVO warrantyInsuredVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询合同被保人
    * @param warrantyInsuredId 合同信息ID
    * @return WarrantyInsuredVO
    */
    WarrantyInsuredVO findById(String warrantyInsuredId);

    /**
     * @Description 合同被保人新增
     * @param warrantyInsuredVO 合同被保人查询条件
     * @return WarrantyInsuredVO
     */
    WarrantyInsuredVO save(WarrantyInsuredVO warrantyInsuredVO);

    /**
     * @Description 合同被保人修改
     * @param warrantyInsuredVO 合同被保人对象
     * @return WarrantyInsuredVO
     */
    Boolean update(WarrantyInsuredVO warrantyInsuredVO);

    /**
     * @Description 合同被保人删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询合同被保人列表
     * @param warrantyInsuredVO 查询条件
     * @return: List<WarrantyInsuredVO>
     */
    List<WarrantyInsuredVO> findList(WarrantyInsuredVO warrantyInsuredVO);
}
