package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.CombinationInsurance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CombinationInsuranceVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：组合保险服务类
 */
public interface ICombinationInsuranceService extends IService<CombinationInsurance> {

    /**
     * @Description 多条件查询组合保险分页
     * @param combinationInsuranceVO 组合保险查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CombinationInsuranceVO>
     */
    Page<CombinationInsuranceVO> findPage(CombinationInsuranceVO combinationInsuranceVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询组合保险
    * @param combinationInsuranceId 合同信息ID
    * @return CombinationInsuranceVO
    */
    CombinationInsuranceVO findById(String combinationInsuranceId);

    /**
     * @Description 组合保险新增
     * @param combinationInsuranceVO 组合保险查询条件
     * @return CombinationInsuranceVO
     */
    CombinationInsuranceVO save(CombinationInsuranceVO combinationInsuranceVO);

    /**
     * @Description 组合保险修改
     * @param combinationInsuranceVO 组合保险对象
     * @return CombinationInsuranceVO
     */
    Boolean update(CombinationInsuranceVO combinationInsuranceVO);

    /**
     * @Description 组合保险删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询组合保险列表
     * @param combinationInsuranceVO 查询条件
     * @return: List<CombinationInsuranceVO>
     */
    List<CombinationInsuranceVO> findList(CombinationInsuranceVO combinationInsuranceVO);
}
