package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.InsuranceApprove;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceApproveVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：保批信息服务类
 */
public interface IInsuranceApproveService extends IService<InsuranceApprove> {

    /**
     * @Description 多条件查询保批信息分页
     * @param insuranceApproveVO 保批信息查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceApproveVO>
     */
    Page<InsuranceApproveVO> findPage(InsuranceApproveVO insuranceApproveVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保批信息
    * @param insuranceApproveId 合同信息ID
    * @return InsuranceApproveVO
    */
    InsuranceApproveVO findById(String insuranceApproveId);

    /**
     * @Description 保批信息新增
     * @param insuranceApproveVO 保批信息查询条件
     * @return InsuranceApproveVO
     */
    InsuranceApproveVO save(InsuranceApproveVO insuranceApproveVO);

    /**
     * @Description 保批信息修改
     * @param insuranceApproveVO 保批信息对象
     * @return InsuranceApproveVO
     */
    Boolean update(InsuranceApproveVO insuranceApproveVO);

    /**
     * @Description 保批信息删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保批信息列表
     * @param insuranceApproveVO 查询条件
     * @return: List<InsuranceApproveVO>
     */
    List<InsuranceApproveVO> findList(InsuranceApproveVO insuranceApproveVO);

    /**
     * 根据合同编号获取报批信息
     * @param warrantyNo
     * @return
     */
    List<InsuranceApproveVO> findByWarrantyNo(String warrantyNo);
}
