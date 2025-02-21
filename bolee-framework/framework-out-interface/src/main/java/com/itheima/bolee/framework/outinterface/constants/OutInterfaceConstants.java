package com.itheima.bolee.framework.outinterface.constants;

import com.itheima.bolee.framework.outinterface.config.OutInterfaceSourceConfig;


/**
 * OutInterfaceConstants
 *
 * @author: wgl
 * @describe: 外部数据源常量类
 * @date: 2022/12/28 10:10
 */
public class OutInterfaceConstants {

    public static class BAI_DU_CLOUD{

        public final static String BANK_CARD_OCR_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard";


        public final static String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?client_id=%s&client_secret=%s&grant_type=client_credentials";

        public static String getTokenUrl(String ak,String sk){
            return String.format(TOKEN_URL,ak,sk);
        }
    }

}
