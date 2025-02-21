package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.SafeguardVO;
import com.itheima.bolee.insurance.pojo.PlanSafeguard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.PlanSafeguardVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * @Description：方案保障项服务类
 */
public interface IPlanSafeguardService extends IService<PlanSafeguard> {

    /**
     * @Description 多条件查询方案保障项分页
     * @param planSafeguardVO 方案保障项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<PlanSafeguardVO>
     */
    Page<PlanSafeguardVO> findPage(PlanSafeguardVO planSafeguardVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询方案保障项
    * @param planSafeguardId 合同信息ID
    * @return PlanSafeguardVO
    */
    PlanSafeguardVO findById(String planSafeguardId);

    /**
     * @Description 方案保障项新增
     * @param planSafeguardVO 方案保障项查询条件
     * @return PlanSafeguardVO
     */
    PlanSafeguardVO save(PlanSafeguardVO planSafeguardVO);

    /**
     * @Description 方案保障项修改
     * @param planSafeguardVO 方案保障项对象
     * @return PlanSafeguardVO
     */
    Boolean update(PlanSafeguardVO planSafeguardVO);

    /**
     * @Description 方案保障项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询方案保障项列表
     * @param planSafeguardVO 查询条件
     * @return: List<PlanSafeguardVO>
     */
    List<PlanSafeguardVO> findList(PlanSafeguardVO planSafeguardVO);

    /**
     * @description 按PlanId删除方案规格
     * @param planIds 方案IDS
     * @return: List<PlanSafeguardVO>
     */
    boolean deleteInPlanIds(List<Long> planIds);

    /**
     * @description 按PlanId删除方案规格
     * @param planIds 方案IDs
     * @return: List<PlanSafeguardVO>
     */
    boolean deleteInPlanId(List<Long> planIds);

    /**
     * @description 按PlanIds查找方案规格
     * @param planIds 方案IDs
     * @return: List<PlanSafeguardVO>
     */
    List<PlanSafeguardVO> findInPlanId(Set<Long> planIds);

    /**
     * @description 按PlanIds查找方案规格
     * @param planId 方案IDs
     * @return: List<PlanSafeguardVO>
     */
    List<PlanSafeguardVO> findByPlanId(Long planId);


}
