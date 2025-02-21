package com.itheima.bolee.points.service;

import com.itheima.bolee.points.vo.analysis.AvgPersonAmountVO;
import com.itheima.bolee.points.vo.analysis.CcnVO;
import com.itheima.bolee.points.vo.analysis.SaleTopVO;
import com.itheima.bolee.points.vo.analysis.WarrantyNumVO;
import com.itheima.bolee.points.vo.table.StackingChartsVO;

/**
 * @ClassName ISaleReportService.java
 * @Description 日投保明细服务接口
 */
public interface ISaleReportService {

    /**
     * 计划任务：日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureDetailDayJob(String reportTime);

    /**
     * 计划任务：日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureCategoryJob(String reportTime);

    /**
     * 数据展示：销售顶部统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return SaleTopVO
     */
    SaleTopVO saleTop(String startTime, String endTime);

    /**
     * 数据展示：累计保单客户数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return CcnVO
     */
    CcnVO ccn(String startTime, String endTime);

    /**
     * 数据展示：7日|保单数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return WarrantyNumVO
     */
    WarrantyNumVO warrantyNum(String startTime, String endTime);

    /**
     * 数据展示：7日|各分类投保保单
     * @param startTime
     * @param endTime
     * @return StackingChartsVO
     */
    StackingChartsVO doInsureCategory(String startTime, String endTime);

    /**
     * 数据展示：7日|人均保费
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return WarrantyNumVO
     */
    AvgPersonAmountVO avgPersonAmount(String startTime, String endTime);
}
