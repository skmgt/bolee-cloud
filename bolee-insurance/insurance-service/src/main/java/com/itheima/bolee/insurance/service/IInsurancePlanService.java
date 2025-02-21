package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.InsurancePlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsurancePlanVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：保险方案服务类
 */
public interface IInsurancePlanService extends IService<InsurancePlan> {

    /**
     * @Description 多条件查询保险方案分页
     * @param insurancePlanVO 保险方案查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsurancePlanVO>
     */
    Page<InsurancePlanVO> findPage(InsurancePlanVO insurancePlanVO, int pageNum, int pageSize);

    /**
    * @Description 按产品ID和方案ID查找
    * @param insurancePlanId 方案ID
    * @param insuranceId 产品ID
    * @return InsurancePlanVO
    */
    InsurancePlanVO findByIdAndInsuranceId(Long insurancePlanId,Long insuranceId);

    /**
     * @Description 批量保险方案新增
     * @param insurancePlanVOs 保险方案
     * @return InsurancePlanVO
     */
    Boolean save(List<InsurancePlanVO> insurancePlanVOs);

    /**
     * @Description 保险方案修改
     * @param insurancePlanVOs 保险方案对象
     * @return InsurancePlanVO
     */
    Boolean update(List<InsurancePlanVO> insurancePlanVOs);

    /**
     * @Description 保险方案删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保险方案列表
     * @param insurancePlanVO 查询条件
     * @return: List<InsurancePlanVO>
     */
    List<InsurancePlanVO> findList(InsurancePlanVO insurancePlanVO);

}
