package com.itheima.bolee.sms.adapter;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;

/**
 * @ClassName SmsTemplateVOAdapter.java
 * @Description 模板适配器接口
 */
public interface SmsTemplateAdapter {

    /***
     * @description 申请模板
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplateVO addSmsTemplate(SmsTemplateVO smsTemplateVO);

    /***
     * @description 删除模板
     * @param checkedIds 模板信息
     * @return
     */
    Boolean deleteSmsTemplate(String[] checkedIds);

    /***
     * @description 修改模板
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplateVO modifySmsTemplate(SmsTemplateVO smsTemplateVO);

    /***
     * @description 查询模板审核状态
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplateVO querySmsTemplate(SmsTemplateVO smsTemplateVO);


}
