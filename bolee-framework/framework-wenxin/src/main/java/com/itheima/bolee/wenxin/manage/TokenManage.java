package com.itheima.bolee.wenxin.manage;

import com.itheima.bolee.wenxin.chains.impl.TokenChain;
import com.itheima.bolee.wenxin.dto.ParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * TokenManage
 *
 * @author: wgl
 * @describe: 文心一言token管理器
 * @date: 2022/12/28 10:10
 */
public class TokenManage implements CommandLineRunner {

    private static String accessToken;
    @Autowired
    private TokenChain tokenChain;


    public static String getAccessToken(){
        return accessToken;
    }

    @Override
    public void run(String... args) throws Exception {
        //服务启动时自动获取token
        ParamsDTO paramsDTO = new ParamsDTO();
        tokenChain.doRequest(paramsDTO);
        accessToken = paramsDTO.getAccessToken();
    }
}
