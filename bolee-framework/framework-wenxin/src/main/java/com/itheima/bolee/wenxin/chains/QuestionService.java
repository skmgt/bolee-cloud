package com.itheima.bolee.wenxin.chains;


import com.itheima.bolee.wenxin.dto.ParamsDTO;
import com.itheima.bolee.wenxin.dto.RequestResultDTO;

/**
 * 问答统一模板类
 */
public interface QuestionService {

    /**
     * 向百度文心一言发送请求
     */
    public RequestResultDTO doRequest(ParamsDTO params);

}
