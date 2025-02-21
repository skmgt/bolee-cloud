package com.itheima.bolee.wenxin.chains.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.wenxin.chains.QuestionService;
import com.itheima.bolee.wenxin.config.WenXinGtpSourceConfig;
import com.itheima.bolee.wenxin.constants.WenXinGptConstants;
import com.itheima.bolee.wenxin.dto.ParamsDTO;
import com.itheima.bolee.wenxin.dto.RequestResultDTO;
import com.itheima.bolee.wenxin.exception.WenXinGptException;
import com.itheima.bolee.wenxin.manage.TokenManage;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * TokenChain
 *
 * @author: wgl
 * @describe: 获取文心一言获取token的接口
 * @date: 2022/12/28 10:10
 */
@Component
public class TokenChain implements QuestionService {

    @Autowired
    private WenXinGtpSourceConfig wenXinGtpSourceConfig;

    @Override
    public RequestResultDTO doRequest(ParamsDTO params) {
        if (StrUtil.isNotEmpty(TokenManage.getAccessToken())) {
            params.setAccessToken(TokenManage.getAccessToken());
        } else {
            synchronized (this) {
                if (StrUtil.isEmpty(TokenManage.getAccessToken())) {
                    String tokenUrl = WenXinGptConstants.getTokenUrl(wenXinGtpSourceConfig);
                    try {
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url(tokenUrl)
                                .method("POST", body)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Accept", "application/json")
                                .build();
                        OkHttpClient client = new OkHttpClient().newBuilder().build();
                        Response response = client.newCall(request).execute();
                        Map<String, Object> tokenDTO = JSONUtil.toBean(response.body().string(), Map.class);
                        if (ObjectUtil.isNull(tokenDTO)) {
                            throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_TOKEN_REQUEST_ERROR);
                        }
                        String accessToken = tokenDTO.get("access_token").toString();
                        params.setAccessToken(accessToken);
                    } catch (Exception e) {
                        throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_TOKEN_REQUEST_ERROR);
                    }
                }else{
                    params.setAccessToken(TokenManage.getAccessToken());
                }
            }
        }
        return new RequestResultDTO();
    }
}
