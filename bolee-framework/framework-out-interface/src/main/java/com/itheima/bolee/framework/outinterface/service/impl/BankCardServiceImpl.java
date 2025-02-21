package com.itheima.bolee.framework.outinterface.service.impl;

import com.itheima.bolee.framework.outinterface.config.OutInterfaceSourceConfig;
import com.itheima.bolee.framework.outinterface.constants.OutInterfaceConstants;
import com.itheima.bolee.framework.outinterface.service.BankCardService;
import com.itheima.bolee.framework.outinterface.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * BankCardServiceImpl
 *
 * @author: wgl
 * @describe: 银行卡OCR业务层
 * @date: 2022/12/28 10:10
 */
@Service
public class BankCardServiceImpl implements BankCardService {


    @Autowired
    OutInterfaceSourceConfig outInterfaceSourceConfig;

    @Override
    public String bankCardOcr(String base64Image) throws Exception {
        // 请求url
        String url = OutInterfaceConstants.BAI_DU_CLOUD.BANK_CARD_OCR_URL;
        // 本地文件路径
        String imgParam = URLEncoder.encode(base64Image, "UTF-8");
        String param = "image=" + imgParam;
        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//        String accessToken = outInterfaceSourceConfig.getAccessToken();
        String accessToken = "24.e43b196c823c865919eac7dd088a0215.2592000.1696557649.282335-38868704";
        String result = HttpUtil.post(url, accessToken, param);
        return result;
    }
}
