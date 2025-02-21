package com.itheima.bolee.wenxin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.wenxin.chains.TotalChain;
import com.itheima.bolee.wenxin.config.WenXinGtpSourceConfig;
import com.itheima.bolee.wenxin.dto.ParamsDTO;
import com.itheima.bolee.wenxin.dto.RequestResultDTO;
import com.itheima.bolee.wenxin.exception.WenXinGptException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * WenXinGptServiceImpl
 *
 * @author: wgl
 * @describe: 统一的请求处理
 * @date: 2022/12/28 10:10
 */
public class WenXinGptServiceImpl implements WenXinService {

    @Autowired
    private TotalChain totalChain;
    @Autowired
    private WenXinGtpSourceConfig gptConfig;

    /**
     * 向文心一言发送请求
     *
     * @param question
     * @return
     */
    @Override
    public String askQuestion(String question) {
        try {
            RequestResultDTO requestResultDTO = totalChain.doRequest(new ParamsDTO());
            if(StrUtil.isNotEmpty(requestResultDTO.getResult())){
                String res = requestResultDTO.getResult().split("```json")[1];
                //去处转义字符
                char[] charsToRemove = {'\r', '\n','\\','`'};
                res = StrUtil.removeAll(res, charsToRemove);
                return res;
            }else{
                throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_REQUEST_ERROR);
            }
        } catch (WenXinGptException e) {
            throw e;
        } catch (Exception e) {
            throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_REQUEST_ERROR);
        }
    }
}
