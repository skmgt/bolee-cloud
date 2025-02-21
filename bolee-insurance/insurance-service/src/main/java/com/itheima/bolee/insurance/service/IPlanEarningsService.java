package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.PlanEarnings;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.PlanEarningsVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * @Description：方案给付服务类
 */
public interface IPlanEarningsService extends IService<PlanEarnings> {

    /**
     * @Description 多条件查询方案给付分页
     * @param planEarningsVO 方案给付查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<PlanEarningsVO>
     */
    Page<PlanEarningsVO> findPage(PlanEarningsVO planEarningsVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询方案给付
    * @param planEarningsId 合同信息ID
    * @return PlanEarningsVO
    */
    PlanEarningsVO findById(String planEarningsId);

    /**
     * @Description 方案给付新增
     * @param planEarningsVO 方案给付查询条件
     * @return PlanEarningsVO
     */
    PlanEarningsVO save(PlanEarningsVO planEarningsVO);

    /**
     * @Description 方案给付修改
     * @param planEarningsVO 方案给付对象
     * @return PlanEarningsVO
     */
    Boolean update(PlanEarningsVO planEarningsVO);

    /**
     * @Description 方案给付删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询方案给付列表
     * @param planEarningsVO 查询条件
     * @return: List<PlanEarningsVO>
     */
    List<PlanEarningsVO> findList(PlanEarningsVO planEarningsVO);

    /**
     * @description 按PlanId查找方案给付计划
     * @param planId 方案IDs
     * @return: PlanEarningsVO
     */
    PlanEarningsVO findByPlanId(Long planId);

    /**
     * @description 按PlanId删除方案给付计划
     * @param planIds 方案IDs
     * @return: PlanEarningsVO
     */
    boolean deleteInPlanIds(List<Long> planIds);

    /**
     * @description 按PlanIds删除方案给付计划
     * @param planIds 方案IDs
     * @return: PlanEarningsVO
     */
    boolean deleteInPlanId(List<Long> planIds);

    /**
     * @description 按PlanIds查找方案给付计划
     * @param planIds 方案IDs
     * @return: PlanEarningsVO
     */
    List<PlanEarningsVO> findInPlanId(Set<Long> planIds);
}
