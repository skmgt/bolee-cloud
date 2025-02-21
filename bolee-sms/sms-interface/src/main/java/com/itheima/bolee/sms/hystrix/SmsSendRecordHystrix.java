package com.itheima.bolee.sms.hystrix;

import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.feign.SmsSendRecordFegin;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SmsSendRecordHystrix.java
 * @Description TODO
 */
@Component
public class SmsSendRecordHystrix implements SmsSendRecordFegin {


    @Override
    public List<SmsSendRecordVO> callBackSmsSendRecords() {
        return null;
    }
}
