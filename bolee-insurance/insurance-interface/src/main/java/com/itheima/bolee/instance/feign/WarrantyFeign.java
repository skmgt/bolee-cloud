package com.itheima.bolee.instance.feign;


import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerInsuranceDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerSexDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisInsuranceTypeDTO;
import com.itheima.bolee.instance.hystrix.WarrantyHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


/**
 * WarrantyFeign
 */
@FeignClient(value = "insurance-mgt",fallback = WarrantyHystrix.class)
public interface WarrantyFeign {


    /**
     * 系统监听：清理合同保单
     * @return
     */
    @PostMapping("warranty-feign/clean-warranty/{warrantyNo}")
    public Boolean cleanWarranty(@PathVariable("warrantyNo") String warrantyNo);

    /**
     * 计划任务:周期代扣
     * @return
     */
    @PostMapping("warranty-feign/periodic-pay")
    public Boolean periodicPay();

    /**
     * 系统监听:同步合同支付结果
     * @param orderNo WarrantyOrderVO的订单编号
     * @return
     */
    @PostMapping("warranty-feign/sync-payment/{orderNo}/{tradeState}")
    public Boolean syncPayment(@PathVariable("orderNo") String orderNo,@PathVariable("tradeState") String tradeState);

}
