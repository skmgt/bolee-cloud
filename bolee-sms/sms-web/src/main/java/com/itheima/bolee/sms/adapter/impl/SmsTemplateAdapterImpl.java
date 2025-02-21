package com.itheima.bolee.sms.adapter.impl;

import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.sms.adapter.SmsTemplateAdapter;
import com.itheima.bolee.sms.handler.SmsTemplateHandler;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SmsTemplateAdapterImpl.java
 * @Description 模板适配器实现
 */
@Service("smsTemplateAdapter")
public class SmsTemplateAdapterImpl implements SmsTemplateAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    ISmsTemplateService smsTemplateService;

    private static Map<String,String> smsTemplateHandlers =new HashMap<>();

    static {
        smsTemplateHandlers.put(SmsConstant.ALIYUN_SMS,"aliyunTemplateHandler");
        smsTemplateHandlers.put(SmsConstant.TENCENT_SMS,"tencentTemplateHandler");
        smsTemplateHandlers.put(SmsConstant.BAIDU_SMS,"baiduTemplateHandler");
    }

    @Override
    public SmsTemplateVO addSmsTemplate(SmsTemplateVO smsTemplateVO) {
        String channelLabel = smsTemplateVO.getChannelLabel();
        String stringSmsTemplateHandler = smsTemplateHandlers.get(channelLabel);
        SmsTemplateHandler smsTemplateHandler =registerBeanHandler
                .getBean(stringSmsTemplateHandler,SmsTemplateHandler.class);
        return BeanConv.toBean(smsTemplateHandler.addSmsTemplate(smsTemplateVO),SmsTemplateVO.class);
    }

    @Override
    public Boolean deleteSmsTemplate(String[] checkedIds) {
        for (String checkedId : checkedIds) {
            SmsTemplateVO smsTemplateVO = BeanConv.toBean(smsTemplateService.getById(checkedId), SmsTemplateVO.class);
            String channelLabel = smsTemplateVO.getChannelLabel();
            String stringSmsTemplateHandler = smsTemplateHandlers.get(channelLabel);
            SmsTemplateHandler smsTemplateHandler = registerBeanHandler
                    .getBean(stringSmsTemplateHandler, SmsTemplateHandler.class);
            smsTemplateHandler.deleteSmsTemplate(smsTemplateVO);
        }
        return true;
    }

    @Override
    public SmsTemplateVO modifySmsTemplate(SmsTemplateVO smsTemplateVO) {
        String channelLabel = smsTemplateVO.getChannelLabel();
        String stringSmsTemplateHandler = smsTemplateHandlers.get(channelLabel);
        SmsTemplateHandler smsTemplateHandler =registerBeanHandler
                .getBean(stringSmsTemplateHandler,SmsTemplateHandler.class);
        return BeanConv.toBean(smsTemplateHandler.modifySmsTemplate(smsTemplateVO),SmsTemplateVO.class);
    }

    @Override
    public SmsTemplateVO querySmsTemplate(SmsTemplateVO smsTemplateVO) {
        String channelLabel = smsTemplateVO.getChannelLabel();
        String stringSmsTemplateHandler = smsTemplateHandlers.get(channelLabel);
        SmsTemplateHandler smsTemplateHandler =registerBeanHandler
                .getBean(stringSmsTemplateHandler,SmsTemplateHandler.class);
        return BeanConv.toBean(smsTemplateHandler.querySmsTemplate(smsTemplateVO),SmsTemplateVO.class);
    }
}
