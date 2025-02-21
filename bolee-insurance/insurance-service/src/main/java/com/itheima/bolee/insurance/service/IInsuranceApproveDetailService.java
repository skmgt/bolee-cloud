package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.InsuranceApproveDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceApproveDetailVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：保批信息服务类
 */
public interface IInsuranceApproveDetailService extends IService<InsuranceApproveDetail> {

    /**
     * @Description 多条件查询保批信息分页
     * @param insuranceApproveDetailVO 保批信息查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceApproveDetailVO>
     */
    Page<InsuranceApproveDetailVO> findPage(InsuranceApproveDetailVO insuranceApproveDetailVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保批信息
    * @param insuranceApproveDetailId 合同信息ID
    * @return InsuranceApproveDetailVO
    */
    InsuranceApproveDetailVO findById(String insuranceApproveDetailId);

    /**
     * @Description 保批信息新增
     * @param insuranceApproveDetailVO 保批信息查询条件
     * @return InsuranceApproveDetailVO
     */
    InsuranceApproveDetailVO save(InsuranceApproveDetailVO insuranceApproveDetailVO);

    /**
     * @Description 保批信息修改
     * @param insuranceApproveDetailVO 保批信息对象
     * @return InsuranceApproveDetailVO
     */
    Boolean update(InsuranceApproveDetailVO insuranceApproveDetailVO);

    /**
     * @Description 保批信息删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保批信息列表
     * @param insuranceApproveDetailVO 查询条件
     * @return: List<InsuranceApproveDetailVO>
     */
    List<InsuranceApproveDetailVO> findList(InsuranceApproveDetailVO insuranceApproveDetailVO);
}
