package com.itheima.bolee.sms.adapter.impl;

import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.sms.adapter.SmsSignAdapter;
import com.itheima.bolee.sms.handler.SmsSignHandler;
import com.itheima.bolee.sms.service.ISmsSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SmsSignAdapterImpl.java
 * @Description 签名适配器实现
 */
@Service("smsSignAdapter")
public class SmsSignAdapterImpl implements SmsSignAdapter {

    @Autowired
    ISmsSignService smsSignService;

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    //处理类map容器定义
    private static Map<String,String> SmsSignHandlers =new HashMap<>();

    //把SmsSignHandler所有的实现类都维护起来，项目启动时加载
    static {
        SmsSignHandlers.put(SmsConstant.ALIYUN_SMS,"aliyunSmsSignHandler");
        SmsSignHandlers.put(SmsConstant.TENCENT_SMS,"tencentSmsSignHandler");
        SmsSignHandlers.put(SmsConstant.BAIDU_SMS,"baiduSmsSignHandler");
    }

    @Override
    public SmsSignVO addSmsSign(SmsSignVO smsSignVO) {
        String channelLabel = smsSignVO.getChannelLabel();
        String stringSmsSignHandler = SmsSignHandlers.get(channelLabel);
        SmsSignHandler smsSignHandler =registerBeanHandler.getBean(stringSmsSignHandler,SmsSignHandler.class);
        return BeanConv.toBean(smsSignHandler.addSmsSign(smsSignVO),SmsSignVO.class);
    }

    @Override
    @Transactional
    public Boolean deleteSmsSign(String[] checkedIds) {
        for (String checkedId : checkedIds) {
            SmsSignVO smsSignVO = BeanConv.toBean(smsSignService.getById(checkedId), SmsSignVO.class);
            String channelLabel = smsSignVO.getChannelLabel();
            String stringSmsSignHandler = SmsSignHandlers.get(channelLabel);
            SmsSignHandler smsSignHandler =registerBeanHandler.getBean(stringSmsSignHandler,SmsSignHandler.class);
            smsSignHandler.deleteSmsSign(smsSignVO);
        }
        return true;
    }

    @Override
    public SmsSignVO modifySmsSign(SmsSignVO smsSignVO) {
        String channelLabel = smsSignVO.getChannelLabel();
        String stringSmsSignHandler = SmsSignHandlers.get(channelLabel);
        SmsSignHandler smsSignHandler =registerBeanHandler.getBean(stringSmsSignHandler,SmsSignHandler.class);
        return BeanConv.toBean(smsSignHandler.modifySmsSign(smsSignVO),SmsSignVO.class);
    }

    @Override
    public SmsSignVO querySmsSign(SmsSignVO smsSignVO) {
        String channelLabel = smsSignVO.getChannelLabel();
        String stringSmsSignHandler = SmsSignHandlers.get(channelLabel);
        SmsSignHandler smsSignHandler =registerBeanHandler.getBean(stringSmsSignHandler,SmsSignHandler.class);
        return BeanConv.toBean(smsSignHandler.querySmsSign(smsSignVO),SmsSignVO.class);
    }
}
