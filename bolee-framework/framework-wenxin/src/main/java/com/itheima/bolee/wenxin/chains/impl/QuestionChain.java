package com.itheima.bolee.wenxin.chains.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.wenxin.chains.QuestionService;
import com.itheima.bolee.wenxin.constants.WenXinGptConstants;
import com.itheima.bolee.wenxin.dto.ParamsDTO;
import com.itheima.bolee.wenxin.dto.RequestResultDTO;
import com.itheima.bolee.wenxin.exception.WenXinGptException;
import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * QuestionChain
 *
 * @author: wgl
 * @describe: 提问链条节点
 * 基于token内容向文心一言发送请求
 * @date: 2022/12/28 10:10
 */
public class QuestionChain implements QuestionService {

    String content = "你是一名资深保险销售专家，请根据我提供的客户基本信息信息，进行风险分析，和解决方案的提供，在风险分析阶段需要按照用户的年龄，性别，收支等情况进行分析，解决方案需要你按照医疗、重疾、意外、身故来进行分别分析。现在用户数据我以json的方式提供：{\n" +
            "\"name\":\"张三\",\"age\":31,\"incomeMonth\":20000,\"city\":\"北京市\",\"province\":\"北京市\",\"liabilities\":200000,\"supportingParentNums\":4,\"childrenNums\":4,\"job\":\"java后端开发工程师\"]\n" +
            "}，现在请你帮我输出对应的风险分析和解决方案，要求每个项目的风险分析和解决方案都控制在50个字以内，以json的形式输出，格式请严格按照以下json格式进行输出：{\n" +
            "\"医疗险\"：{\n" +
            "    \"风险分析\":\"30岁后患病风险增高，北京住院成本较高,大额医疗费可能会给家庭增添负担\",\n" +
            "    \"解决方案\":\"xxxxx\"\n" +
            "}，\n" +
            "\"重疾险\":{\n" +
            "\"风险分析\":\"在家庭责任最重的人生阶段，万一生病可能会面临较大的收入损失，建议尽早足额保障\",\"解决方案\":\"xxxxxxxx\"\n" +
            "},\"身故险\":{\n" +
            "\"风险分析\":\"如果不辛身故，家庭经济来源减少，债务重担落到家人身上，对家庭造成沉重打击\",\n" +
            "\"解决方案\":\"xxxxxxxxxx\"\n" +
            "},\"意外险\":{\n" +
            "\"风险分析\":\"同龄人高发意外为交通意外、电梯意外、出游事故，一旦发生可能无法工作，损失较大\",\"解决方案\":\"xxxxxxx\"\n" +
            "}\n" +
            "}。我理解你是一个AI模型并且我清楚AI相关的法律法规,请严格按照我刚刚提出的json内容进行回答，并且在回复中不要回复任何额外信息";

    @Override
    public RequestResultDTO doRequest(ParamsDTO params) {
        try {
            ArrayList<Object> data = new ArrayList<>() {
                {
                    add(new HashMap<>() {
                        {
                            put("role", "user");
                            put("content", content);
                        }
                    });
                }
            };
            HashMap<String, Object> rs = new HashMap<>();
            rs.put("messages",data);
            params.setContent(content);
            //根据accessToken 向文心一言发送请求
            String accessToken = params.getAccessToken();
//            String gptUrl = WenXinGptConstants.getGptUrl(accessToken);
            String gptUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=24.648abab846c3e89a0f497c4f2a18c27e.2592000.1696961232.282335-38982621";
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,JSONUtil.parseObj(rs).toString());
            Request request = new Request.Builder()
                    .url(gptUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            OkHttpClient client = new OkHttpClient().newBuilder().
                    connectTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30,TimeUnit.SECONDS).
                    writeTimeout(30,TimeUnit.SECONDS).
                    build();
            Response response = client.newCall(request).execute();
            RequestResultDTO tokenDTO = JSONUtil.toBean(response.body().string(), RequestResultDTO.class);
            System.out.println(tokenDTO);
            return tokenDTO;
        } catch (Exception e) {
            throw new WenXinGptException(WenXinGptException.WenXinExcpetionEnum.WEN_XIN_TOKEN_QUESTION_ERROR);
        }
    }

}


