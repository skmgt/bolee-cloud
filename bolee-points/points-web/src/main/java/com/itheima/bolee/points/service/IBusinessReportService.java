package com.itheima.bolee.points.service;

import com.itheima.bolee.points.vo.analysis.DoInsureFailDpvVO;
import com.itheima.bolee.points.vo.table.FunnelPlotChartsVO;
import com.itheima.bolee.points.vo.table.StackingChartsVO;

/**
 * @ClassName IBusinessReportService.java
 * @Description 业务统计接口
 */
public interface IBusinessReportService {

    /**
     * 计划任务：日投保访问页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureDpvJob(String reportTime);

    /**
     * 计划任务：日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureDuvJob(String reportTime);

    /**
     * 计划任务：日保险分类访问页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean categoryDpvJob(String reportTime);

    /**
     * 计划任务：性别日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureGenderDuvJob(String reportTime);

    /**
     * 计划任务：城市日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureCityDuvJob(String reportTime);

    /**
     * 计划任务：投保转换率
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureConversionDpvJob(String reportTime);

    /**
     * 计划任务：日投保访问失败页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean doInsureFailDpvJob(String reportTime);

    /**
     * 数据展示：7日|点击立即投保用户数/次数
     * @param startTime
     * @param endTime
     * @return
     */
    StackingChartsVO doInsureDpvDuv(String startTime, String endTime);

    /**
     * 数据展示：7日|各险种点击率
     * @param startTime
     * @param endTime
     * @return
     */
    StackingChartsVO categoryDpv(String startTime, String endTime);

    /**
     * 数据展示：30日|投保用户性别分布
     * @param startTime
     * @param endTime
     * @return
     */
    StackingChartsVO doInsureGenderDuv(String startTime, String endTime);

    /**
     * 数据展示：30日|投保用户城市分布
     * @param startTime
     * @param endTime
     * @return
     */
    StackingChartsVO doInsureCityDuv(String startTime, String endTime);

    /**
     * 数据展示：投保流程转换漏斗
     * @param startTime
     * @param endTime
     * @return
     */
    FunnelPlotChartsVO doInsureConversionDpv(String startTime, String endTime);

    /**
     * 数据展示：30日|投保提交失败次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    DoInsureFailDpvVO doInsureFailDpv(String startTime, String endTime);
}
