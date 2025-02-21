package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.HealthAssessment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.HealthAssessmentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：评估类目服务类
 */
public interface IHealthAssessmentService extends IService<HealthAssessment> {

    /**
     * @Description 多条件查询评估类目分页
     * @param healthAssessmentVO 评估类目查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<HealthAssessmentVO>
     */
    Page<HealthAssessmentVO> findPage(HealthAssessmentVO healthAssessmentVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询评估类目
    * @param healthAssessmentId 合同信息ID
    * @return HealthAssessmentVO
    */
    HealthAssessmentVO findById(String healthAssessmentId);

    /**
     * @Description 评估类目新增
     * @param healthAssessmentVO 评估类目查询条件
     * @return HealthAssessmentVO
     */
    HealthAssessmentVO save(HealthAssessmentVO healthAssessmentVO);

    /**
     * @Description 评估类目修改
     * @param healthAssessmentVO 评估类目对象
     * @return HealthAssessmentVO
     */
    Boolean update(HealthAssessmentVO healthAssessmentVO);

    /**
     * @Description 评估类目删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询评估类目列表
     * @param healthAssessmentVO 查询条件
     * @return: List<HealthAssessmentVO>
     */
    List<HealthAssessmentVO> findList(HealthAssessmentVO healthAssessmentVO);
}
