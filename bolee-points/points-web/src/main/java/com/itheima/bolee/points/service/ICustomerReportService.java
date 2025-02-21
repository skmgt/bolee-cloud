package com.itheima.bolee.points.service;

import com.itheima.bolee.points.vo.analysis.DauTimeVO;
import com.itheima.bolee.points.vo.analysis.DauVO;
import com.itheima.bolee.points.vo.analysis.DnuVO;
import com.itheima.bolee.points.vo.table.LineChartsVO;
import com.itheima.bolee.points.vo.table.PiChartsVO;
import com.itheima.bolee.points.vo.table.StackingChartsVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @ClassName ICustomerReport.java
 * @Description 用户统计接口
 */
public interface ICustomerReportService {


    /**
     * 计划任务：日新增用户数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dnuJob(String reportTime);

    /**
     * 计划任务：日新增注册用户归属城市
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dnuCityJob(String reportTime);

    /**
     * 计划任务：用户日活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dauJob(String reportTime);

    /**
     * 计划任务：用户每时活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dauTimeJob(String reportTime);

    /**
     * 计划任务：日访问量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dpvJob(String reportTime);

    /**
     * 计划任务：日用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean duvJob(String reportTime);

    /**
     * 计划任务：用户日活跃数范围
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    Boolean dauRangeJob(String reportTime);

    /**
     * 数据展示：日新增用户数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DnuVO
     */
    DnuVO dnu(String startTime,String endTime);

    /**
     * 数据展示：日新增用户所属城市
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DnuVO
     */
    List<PiChartsVO> dnuCity(String startTime, String endTime);

    /**
     * 数据展示：30日|活跃用户数趋势
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    DauVO dau(String startTime, String endTime);

    /**
     * 数据展示：30日|新老活跃用户结构
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    StackingChartsVO newOldDau(String startTime, String endTime);

    /**
     * 数据展示：30日|每天活跃次数分布
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    LineChartsVO dauRange(String startTime, String endTime);

    /**
     * 数据展示：7日|活跃时段分布
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    DauTimeVO dauTime(String startTime, String endTime);
}
