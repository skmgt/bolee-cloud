package com.itheima.bolee.sms.handler;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.pojo.SmsTemplate;

/**
 * @ClassName SmsTemplateHandler.java
 * @Description 模板处理器接口
 */
public interface SmsTemplateHandler {

    /***
     * @description 申请模板
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplate addSmsTemplate(SmsTemplateVO smsTemplateVO);

    /***
     * @description 删除模板
     * @param smsTemplateVO 模板信息
     * @return
     */
    Boolean deleteSmsTemplate(SmsTemplateVO smsTemplateVO);

    /***
     * @description 修改模板
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplate modifySmsTemplate(SmsTemplateVO smsTemplateVO);

    /***
     * @description 查询模板审核状态
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplate querySmsTemplate(SmsTemplateVO smsTemplateVO);


}
