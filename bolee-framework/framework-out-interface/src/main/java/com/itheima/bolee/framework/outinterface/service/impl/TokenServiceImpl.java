package com.itheima.bolee.framework.outinterface.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.outinterface.config.OutInterfaceSourceConfig;
import com.itheima.bolee.framework.outinterface.constants.OutInterfaceConstants;
import com.itheima.bolee.framework.outinterface.dto.TokenDTO;
import com.itheima.bolee.framework.outinterface.service.TokenService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * TokenServiceImpl
 *
 * @author: wgl
 * @describe: 获取Token的业务层
 * @date: 2022/12/28 10:10
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private OutInterfaceSourceConfig outInterfaceSourceConfig;

    @Override
    public TokenDTO getToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(OutInterfaceConstants.BAI_DU_CLOUD.getTokenUrl(outInterfaceSourceConfig.getApiKey(), outInterfaceSourceConfig.getSecretKey()))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Response response = client.newCall(request).execute();
        TokenDTO tokenDTO = JSONUtil.toBean(response.body().string(), TokenDTO.class);
        return tokenDTO;
    }

}
