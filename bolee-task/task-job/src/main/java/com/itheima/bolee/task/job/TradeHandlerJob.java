package com.itheima.bolee.task.job;

import com.itheima.bolee.trade.feign.CommonPayFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName TradeHandlerJob.java
 * @Description TODO
 */
@Component
public class TradeHandlerJob {

    @Autowired
    CommonPayFeign commonPayFeign;

    /**
     * 计划任务：同步支付结果
     * @param param
     * @return
     */
    @XxlJob(value = "trade-sync-payment")
    public ReturnT<String> syncPayment(String param) {
        Boolean responseWrap = commonPayFeign.syncPaymentJob();
        if (responseWrap){
            ReturnT.SUCCESS.setMsg("计划任务：同步支付结果-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("计划任务：同步支付结果-失败");
        return ReturnT.FAIL;
    }

}
