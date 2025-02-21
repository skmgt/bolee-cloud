package com.itheima.bolee.task.job;

import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.feign.SmsSendFeign;
import com.itheima.bolee.sms.feign.SmsSendRecordFegin;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description：同步短信发送结果
 */
@Component
public class SmsSendHandlerJob {

    @Autowired
    SmsSendFeign smsSendFeign;

    @Autowired
    SmsSendRecordFegin smsSendRecordFegin;

    /***
     * @description 短信发生接口同步
     * @param param
     * @return
     */
    @XxlJob(value = "sendHandlerJob")
    public ReturnT<String> execute(String param) {
        List<SmsSendRecordVO> smsSendRecordVOs = smsSendRecordFegin.callBackSmsSendRecords();
        for (SmsSendRecordVO smsSendRecord : smsSendRecordVOs) {
            smsSendFeign.querySendSms(smsSendRecord);
        }
        ReturnT.SUCCESS.setMsg("执行-短信发送同步-成功");
        return ReturnT.SUCCESS;
    }

}
