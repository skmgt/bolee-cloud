package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.InsuranceSieving;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceSievingVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：初筛结果服务类
 */
public interface IInsuranceSievingService extends IService<InsuranceSieving> {

    /**
     * @Description 多条件查询初筛结果分页
     * @param insuranceSievingVO 初筛结果查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<InsuranceSievingVO>
     */
    Page<InsuranceSievingVO> findPage(InsuranceSievingVO insuranceSievingVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询初筛结果
    * @param insuranceSievingId 合同信息ID
    * @return InsuranceSievingVO
    */
    InsuranceSievingVO findById(String insuranceSievingId);

    /**
     * @Description 初筛结果新增
     * @param insuranceSievingVO 初筛结果查询条件
     * @return InsuranceSievingVO
     */
    InsuranceSievingVO save(InsuranceSievingVO insuranceSievingVO);

    /**
     * @Description 初筛结果修改
     * @param insuranceSievingVO 初筛结果对象
     * @return InsuranceSievingVO
     */
    Boolean update(InsuranceSievingVO insuranceSievingVO);

    /**
     * @Description 初筛结果删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询初筛结果列表
     * @param insuranceSievingVO 查询条件
     * @return: List<InsuranceSievingVO>
     */
    List<InsuranceSievingVO> findList(InsuranceSievingVO insuranceSievingVO);
}
