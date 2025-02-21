package com.itheima.bolee.insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceConditionVO;
import com.itheima.bolee.insurance.pojo.InsuranceCondition;

import java.util.List;

/**
 * @Description：保险筛选项服务类
 */
public interface IInsuranceConditionService extends IService<InsuranceCondition> {

    /**
     * @Description 多条件查询保险筛选项分页
     * @param insuranceConditionVO 保险筛选项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceConditionVO>
     */
    Page<InsuranceConditionVO> findPage(InsuranceConditionVO insuranceConditionVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保险筛选项
    * @param insuranceConditionId 合同信息ID
    * @return InsuranceConditionVO
    */
    InsuranceConditionVO findById(String insuranceConditionId);

    /**
     * @Description 保险筛选项新增
     * @param insuranceConditionListVOs 保险筛选项查询条件
     * @return InsuranceConditionVO
     */
    Boolean save(List<InsuranceConditionVO> insuranceConditionListVOs);

    /**
     * @Description 保险筛选项修改
     * @param insuranceConditionVOs 保险筛选项对象
     * @return InsuranceConditionVO
     */
    Boolean update(List<InsuranceConditionVO> insuranceConditionVOs);

    /**
     * @Description 保险筛选项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保险筛选项列表
     * @param insuranceConditionVO 查询条件
     * @return: List<InsuranceConditionVO>
     */
    List<InsuranceConditionVO> findList(InsuranceConditionVO insuranceConditionVO);

}
