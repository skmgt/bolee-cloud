package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.CombinationDetailVO;
import com.itheima.bolee.insurance.pojo.Combination;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CombinationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：组合方案服务类
 */
public interface ICombinationService extends IService<Combination> {

    /**
     * @Description 多条件查询组合方案分页
     * @param combinationVO 组合方案查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CombinationVO>
     */
    Page<CombinationVO> findPage(CombinationVO combinationVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询组合方案
    * @param combinationId 合同信息ID
    * @return CombinationVO
    */
    CombinationVO findById(String combinationId);

    /**
     * @Description 组合方案新增
     * @param combinationVO 组合方案查询条件
     * @return CombinationVO
     */
    CombinationVO save(CombinationVO combinationVO);

    /**
     * @Description 组合方案修改
     * @param combinationVO 组合方案对象
     * @return CombinationVO
     */
    Boolean update(CombinationVO combinationVO);

    /**
     * @Description 组合方案删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询组合方案列表
     * @param combinationVO 查询条件
     * @return: List<CombinationVO>
     */
    List<CombinationVO> findList(CombinationVO combinationVO);

}
