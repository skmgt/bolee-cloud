package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.Condition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.ConditionVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：筛选项服务类
 */
public interface IConditionService extends IService<Condition> {

    /**
     * @Description 多条件查询筛选项分页
     * @param conditionVO 筛选项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<ConditionVO>
     */
    Page<ConditionVO> findPage(ConditionVO conditionVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询筛选项
    * @param conditionId 合同信息ID
    * @return ConditionVO
    */
    ConditionVO findById(String conditionId);

    /**
     * @Description 筛选项新增
     * @param conditionVO 筛选项查询条件
     * @return ConditionVO
     */
    ConditionVO save(ConditionVO conditionVO);

    /**
     * @Description 筛选项修改
     * @param conditionVO 筛选项对象
     * @return ConditionVO
     */
    Boolean update(ConditionVO conditionVO);

    /**
     * @Description 筛选项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询筛选项列表
     * @param conditionVO 查询条件
     * @return: List<ConditionVO>
     */
    List<ConditionVO> findList(ConditionVO conditionVO);

    /**
     * @description 按conditionKeys查询筛选项列表
     * @param conditionKeys 查询条件
     * @return: List<ConditionVO>
     */
    List<ConditionVO> findInConditionKeys(List<String> conditionKeys);
}
