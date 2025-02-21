package com.itheima.bolee.sms.handler;

import com.itheima.bolee.sms.pojo.SmsSign;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;

/**
 * @ClassName SmsSignHandler.java
 * @Description 签名处理器接口
 */
public interface SmsSignHandler {

    /***
     * @description 申请签名
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSign addSmsSign(SmsSignVO smsSignVO);

    /***
     * @description 删除签名
     * @param smsSignVO 签名
     * @return 请求成功
     */
    Boolean deleteSmsSign(SmsSignVO smsSignVO);

    /***
     * @description 修改签名
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSign modifySmsSign(SmsSignVO smsSignVO);

    /***
     * @description 查询签名审核状态
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSign querySmsSign(SmsSignVO smsSignVO);


}
