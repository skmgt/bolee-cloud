package com.itheima.bolee.sms.hystrix;

import com.itheima.bolee.sms.feign.SmsSendFeign;
import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName SmsSendHystrix.java
 * @Description SmsSendFeign的Hystrix
 */
@Component
public class SmsSendHystrix implements SmsSendFeign {


    @Override
    public Boolean sendSmsForMq(SendMessageVO sendMessageVO) {
        return null;
    }

    @Override
    public Boolean sendSms(SendMessageVO sendMessageVO) {
        return null;
    }

    @Override
    public Boolean querySendSms(SmsSendRecordVO smsSendRecordVO) {
        return null;
    }

    @Override
    public Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO) {
        return null;
    }
}
