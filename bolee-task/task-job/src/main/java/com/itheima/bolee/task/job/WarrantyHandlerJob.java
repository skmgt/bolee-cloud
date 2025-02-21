package com.itheima.bolee.task.job;

import com.itheima.bolee.instance.feign.WarrantyFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName WarrantyHandlerJob.java
 * @Description 保险合同处理任务
 */
@Component
public class WarrantyHandlerJob {

    @Autowired
    WarrantyFeign warrantyFeign;

    /**
     * 计划任务:周期代扣
     * @param param
     * @return
     */
    @XxlJob(value = "warranty-periodic-pay")
    public ReturnT<String> execute(String param) {
        Boolean responseWrap = warrantyFeign.periodicPay();
        if (responseWrap){
            ReturnT.SUCCESS.setMsg("计划任务:周期代扣-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("计划任务:周期代扣-失败");
        return ReturnT.FAIL;

    }
}
