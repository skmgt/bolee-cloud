package com.itheima.bolee.sms.service;

import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;

/**
 * @ClassName SmsSendFace.java
 * @Description 邮件发送Face接口
 */
public interface ISmsSendService {

    /***
     * @description 发送短信接口:削峰方式
     * @param sendMessageVO 发送对象
     */
    Boolean sendSmsForMq(SendMessageVO sendMessageVO) ;

    /***
     * @description 发送短信接口:非削峰方式
     * @param sendMessageVO 发送对象
     */
    Boolean sendSms(SendMessageVO sendMessageVO) ;

    /***
     * @description 查询短信接受情况
     * @param smsSendRecordVO 发送记录
     * @return
     */
    Boolean querySendSms(SmsSendRecordVO smsSendRecordVO) ;

    /***
     * @description 重试发送
     * @param smsSendRecordVO
     * @return
     */
    Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO) ;


}
