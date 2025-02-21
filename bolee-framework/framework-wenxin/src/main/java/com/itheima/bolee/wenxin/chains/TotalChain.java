package com.itheima.bolee.wenxin.chains;

import com.itheima.bolee.wenxin.dto.ParamsDTO;
import com.itheima.bolee.wenxin.dto.RequestResultDTO;
import com.itheima.bolee.wenxin.exception.WenXinGptException;
import lombok.extern.java.Log;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TotalChain
 *
 * @author: wgl
 * @describe: 责任链总链路
 * @date: 2022/12/28 10:10
 */
@Log
public class TotalChain implements QuestionService{

    /**
     * 完整的总链路
     * 单利总链路
     */
    private static List<QuestionService> chains = new ArrayList<QuestionService>();

    /**
     * 添加链路节点
     * @param questionService
     */
    public void addChain(QuestionService questionService){
        chains.add(questionService);
    }


    /**
     * 总链路的向文心一眼的调用流程
     * @param params
     * @return
     */
    @Override
    public RequestResultDTO doRequest(ParamsDTO params) {
        RequestResultDTO res = null;
        try {
            for (QuestionService index : chains) {
                RequestResultDTO requestDTO = index.doRequest(params);
                //如果返回为空说明请求失败  停止链路调用
                if (ObjectUtils.isEmpty(requestDTO)) {
                    break;
                }
                res = requestDTO;
            }
        }catch (WenXinGptException e){
            throw e;
        }catch (Exception e) {
            throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_REQUEST_ERROR);
        }
        return res;
    }
}
