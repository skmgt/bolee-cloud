package com.itheima.bolee.sms.feign;

import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.hystrix.SmsSendHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName SmsSendFace.java
 * @Description 邮件发送Face接口
 */
@FeignClient(value = "sms-web",fallback = SmsSendHystrix.class)
public interface SmsSendFeign {

    /***
     * @description 发送短信接口:削峰方式
     * @param sendMessageVO 发送对象
     */
    @PostMapping("sms-send-feign/send-sms-mq")
    Boolean sendSmsForMq(SendMessageVO sendMessageVO);

    /***
     * @description 发送短信接口:非削峰方式
     * @param sendMessageVO 发送对象
     */
    @PostMapping("sms-send-feign/send-sms")
    Boolean sendSms(SendMessageVO sendMessageVO);

    /***
     * @description 查询短信接受情况
     * @param smsSendRecordVO 发送记录
     * @return
     */
    @PostMapping("sms-send-feign/query-send-sms")
    Boolean querySendSms(SmsSendRecordVO smsSendRecordVO);

    /***
     * @description 重试发送
     * @param smsSendRecordVO 发送记录
     * @return
     */
    @PostMapping("sms-send-feign/retry-send-sms")
    Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO);
}
