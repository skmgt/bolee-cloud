package com.itheima.bolee.sms.adapter;

import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;

/**
 * @ClassName SmsSignAdapter.java
 * @Description 签名适配器接口
 */
public interface SmsSignAdapter {

    /***
     * @description 申请签名
     * @param smsSign 签名
     * @return 请求成功
     */
    SmsSignVO addSmsSign(SmsSignVO smsSign);

    /***
     * @description 删除签名
     * @param checkedIds 签名ids
     * @return 请求成功
     */
    Boolean deleteSmsSign(String[] checkedIds);

    /***
     * @description 修改签名
     * @param smsSign 签名
     * @return 请求成功
     */
    SmsSignVO modifySmsSign(SmsSignVO smsSign);

    /***
     * @description 查询签名审核状态
     * @param smsSign 签名
     * @return 请求成功
     */
    SmsSignVO querySmsSign(SmsSignVO smsSign);


}
