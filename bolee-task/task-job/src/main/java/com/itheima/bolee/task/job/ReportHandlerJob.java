package com.itheima.bolee.task.job;

import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.points.feign.BusinessReportFeign;
import com.itheima.bolee.points.feign.CustomerReportFeign;
import com.itheima.bolee.points.feign.SaleReportFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName ReportHandlerJob.java
 * @Description 报表分析处理
 */
@Component
public class ReportHandlerJob {

    @Autowired
    BusinessReportFeign businessReportFeign;

    @Autowired
    CustomerReportFeign customerReportFeign;

    @Autowired
    SaleReportFeign saleReportFeign;

    /**
     * 补全数据
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "complement")
    public ReturnT<String> complement(String param){
        businessReportFeign.doInsureDpvJob(param);
        businessReportFeign.doInsureDuvJob(param);
        businessReportFeign.categoryDpvJob(param);
        businessReportFeign.doInsureGenderDuvJob(param);
        businessReportFeign.doInsureCityDuvJob(param);
        businessReportFeign.doInsureConversionDpvJob(param);
        businessReportFeign.doInsureFailDpvJob(param);
        customerReportFeign.dnuJob(param);
        customerReportFeign.dnuCityJob(param);
        customerReportFeign.dauJob(param);
        customerReportFeign.dauTimeJob(param);
        customerReportFeign.dpvJob(param);
        customerReportFeign.duvJob(param);
        customerReportFeign.dauRangeJob(param);
        saleReportFeign.doInsureDetailsDayJob(param);
        saleReportFeign.doInsureCategoryJob(param);
        ReturnT.SUCCESS.setMsg("补全数据-成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 日投保访页面量
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureDpvJob")
    public ReturnT<String> doInsureDpvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureDpvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日投保访页面量-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日投保访页面量-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日投保用户访问数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureDuvJob")
    ReturnT<String> doInsureDuvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureDuvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日投保用户访问数-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日投保用户访问数-是吧");
        return ReturnT.FAIL;
    }

    /**
     * 日保险分类访问页面量
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "categoryDpvJob")
    ReturnT<String> categoryDpvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.categoryDpvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日保险分类访问页面量-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日保险分类访问页面量-失败");
        return ReturnT.FAIL;
    }

    /**
     * 性别日投保用户访问数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureGenderDuvJob")
    ReturnT<String> doInsureGenderDuvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureGenderDuvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("性别日投保用户访问数-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("性别日投保用户访问数-失败");
        return ReturnT.FAIL;
    }

    /**
     * 城市日投保用户访问数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureCityDuvJob")
    ReturnT<String> doInsureCityDuvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureCityDuvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("城市日投保用户访问数-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("城市日投保用户访问数-失败");
        return ReturnT.FAIL;
    }

    /**
     * 投保转换率
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureConversionDpvJob")
    ReturnT<String> doInsureConversionDpvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureConversionDpvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("城市日投保用户访问数-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("城市日投保用户访问数-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日投保访问失败页面量
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureFailDpvJob")
    ReturnT<String> doInsureFailDpvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = businessReportFeign.doInsureFailDpvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日投保访问失败页面量-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日投保访问失败页面量-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日新增用户数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dnuJob")
    ReturnT<String>  dnuJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dnuJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日新增用户数-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日新增用户数-失败");
        return ReturnT.FAIL;
    }


    /**
     * 日新增注册用户归属城市
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dnuCityJob")
    ReturnT<String> dnuCityJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dnuCityJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日新增注册用户归属城市-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日新增注册用户归属城市-失败");
        return ReturnT.FAIL;
    }

    /**
     * 用户日活跃数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dauJob")
    ReturnT<String> dauJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dauJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日新增注册用户归属城市-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日新增注册用户归属城市-失败");
        return ReturnT.FAIL;
    }

    /**
     * 用户每时活跃数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dauTimeJob")
    ReturnT<String> dauTimeJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dauTimeJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日新增注册用户归属城市-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日新增注册用户归属城市-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日访问量
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dpvJob")
    ReturnT<String> dpvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dpvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日访问量-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日访问量-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日用户访问数
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "duvJob")
    ReturnT<String> duvJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.duvJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日访问量-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日访问量-失败");
        return ReturnT.FAIL;
    }

    /**
     * 用户日活跃数范围
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "dauRangeJob")
    ReturnT<String> dauRangeJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = customerReportFeign.dauRangeJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("用户日活跃数范围-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("用户日活跃数范围-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日投保额度明细
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureDetailsDayJob")
    ReturnT<String> doInsureDetailsDayJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = saleReportFeign.doInsureDetailsDayJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日投保额度明细-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日投保额度明细-失败");
        return ReturnT.FAIL;
    }

    /**
     * 日投保分类明细
     * @param param 统计时间
     * @return 是否执行成功
     */
    @XxlJob(value = "doInsureCategoryJob")
    ReturnT<String> doInsureCategoryJob(String param){
        if (EmptyUtil.isNullOrEmpty(param)){
            param = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Boolean flag = saleReportFeign.doInsureCategoryJob(param);
        if (flag){
            ReturnT.SUCCESS.setMsg("日投保分类明细-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("日投保分类明细-失败");
        return ReturnT.FAIL;
    }
}
