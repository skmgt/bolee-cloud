package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.RiskAnalysis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.RiskAnalysisVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：风险类目服务类
 */
public interface IRiskAnalysisService extends IService<RiskAnalysis> {

    /**
     * @Description 多条件查询风险类目分页
     * @param riskAnalysisVO 风险类目查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<RiskAnalysisVO>
     */
    Page<RiskAnalysisVO> findPage(RiskAnalysisVO riskAnalysisVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询风险类目
    * @param riskAnalysisId 合同信息ID
    * @return RiskAnalysisVO
    */
    RiskAnalysisVO findById(String riskAnalysisId);

    /**
     * @Description 风险类目新增
     * @param riskAnalysisVO 风险类目查询条件
     * @return RiskAnalysisVO
     */
    RiskAnalysisVO save(RiskAnalysisVO riskAnalysisVO);

    /**
     * @Description 风险类目修改
     * @param riskAnalysisVO 风险类目对象
     * @return RiskAnalysisVO
     */
    Boolean update(RiskAnalysisVO riskAnalysisVO);

    /**
     * @Description 风险类目删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询风险类目列表
     * @param riskAnalysisVO 查询条件
     * @return: List<RiskAnalysisVO>
     */
    List<RiskAnalysisVO> findList(RiskAnalysisVO riskAnalysisVO);
}
