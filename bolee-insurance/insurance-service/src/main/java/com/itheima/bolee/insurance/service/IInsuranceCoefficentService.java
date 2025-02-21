package com.itheima.bolee.insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceCoefficentVO;
import com.itheima.bolee.insurance.pojo.InsuranceCoefficent;

import java.util.List;

/**
 * @Description：保险系数项服务类
 */
public interface IInsuranceCoefficentService extends IService<InsuranceCoefficent> {

    /**
     * @Description 多条件查询保险系数项分页
     * @param insuranceCoefficentVO 保险系数项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceCoefficentVO>
     */
    Page<InsuranceCoefficentVO> findPage(InsuranceCoefficentVO insuranceCoefficentVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保险系数项
    * @param insuranceCoefficentId 合同信息ID
    * @return InsuranceCoefficentVO
    */
    InsuranceCoefficentVO findById(String insuranceCoefficentId);

    /**
     * @Description 保险系数项新增
     * @param insuranceCoefficentVOs 保险系数项
     * @return InsuranceCoefficentVO
     */
    Boolean save(List<InsuranceCoefficentVO> insuranceCoefficentVOs);

    /**
     * @Description 保险系数项修改
     * @param insuranceCoefficentVOs 保险系数项对象
     * @return InsuranceCoefficentVO
     */
    Boolean update(List<InsuranceCoefficentVO> insuranceCoefficentVOs);

    /**
     * @Description 保险系数项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保险系数项列表
     * @param insuranceCoefficentVO 查询条件
     * @return: List<InsuranceCoefficentVO>
     */
    List<InsuranceCoefficentVO> findList(InsuranceCoefficentVO insuranceCoefficentVO);

    /**
     * @description ids查询保险系数项列表
     * @param insuranceCoefficentIds 查询条件
     * @return: List<InsuranceCoefficentVO>
     */
    List<InsuranceCoefficentVO> findListByIdsAndInsuranceId(List<Long> insuranceCoefficentIds,Long insuranceId );


}
