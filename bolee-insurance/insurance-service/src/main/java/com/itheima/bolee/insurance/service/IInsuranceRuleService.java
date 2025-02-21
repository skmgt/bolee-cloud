package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.InsuranceRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceRuleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：保险规则服务类
 */
public interface IInsuranceRuleService extends IService<InsuranceRule> {

    /**
     * @Description 多条件查询保险规则分页
     * @param insuranceRuleVO 保险规则查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceRuleVO>
     */
    Page<InsuranceRuleVO> findPage(InsuranceRuleVO insuranceRuleVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保险规则
    * @param insuranceRuleId 合同信息ID
    * @return InsuranceRuleVO
    */
    InsuranceRuleVO findById(String insuranceRuleId);

    /**
     * @Description 保险规则新增
     * @param insuranceRuleVO 保险规则查询条件
     * @return InsuranceRuleVO
     */
    InsuranceRuleVO save(InsuranceRuleVO insuranceRuleVO);

    /**
     * @Description 保险规则修改
     * @param insuranceRuleVO 保险规则对象
     * @return InsuranceRuleVO
     */
    Boolean update(InsuranceRuleVO insuranceRuleVO);

    /**
     * @Description 保险规则删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保险规则列表
     * @param insuranceRuleVO 查询条件
     * @return: List<InsuranceRuleVO>
     */
    List<InsuranceRuleVO> findList(InsuranceRuleVO insuranceRuleVO);
}
