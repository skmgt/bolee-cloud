package com.itheima.bolee.instance.feign;


import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerInsuranceDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerSexDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisInsuranceTypeDTO;
import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.instance.hystrix.AnalysisBusinessHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


/**
 * AnalysisBusinessFeign
 *
 * @author: wgl
 * @describe: 统计分析:业务数据统计分析feign服务
 * @date: 2022/12/28 10:10
 */
@FeignClient(value = "insurance-mgt",fallback = AnalysisBusinessHystrix.class)
public interface AnalysisBusinessFeign {


    /**
     * 统计昨日保单数量DTO
     * @return
     */
    @PutMapping("analysis-feign/analysis-insurance")
    AnalysisCustomerInsuranceDTO countCustomerInstance();

    /**
     * 统计:投保人性别统计
     * @return
     */
    @PutMapping("analysis-feign/analysis-warranty-sex")
    AnalysisCustomerSexDTO countWarrantySex();


    /**
     * 投保统计：投保分类类型统计及投保数量统计
     * @return
     */
    @PutMapping("analysis-feign/analysis-customer-insurance-type")
    List<AnalysisInsuranceTypeDTO> countCustomerInstanceType();
}
