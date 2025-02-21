package com.itheima.bolee.wenxin.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ParamsDTO
 *
 * @author: wgl
 * @describe: 请求参数对象
 * @date: 2022/12/28 10:10
 */
@Data
public class ParamsDTO{

    private String accessToken;

    private String role;
    private String content;

    /**
     * ----------------文心一言提示词构建-------------------
     * 你是一名资深保险销售专家，请根据我提供的客户基本信息信息，进行风险分析，和解决方案的提供，在风险分析阶段需要按照用户的年龄，性别，收支等情况进行分析，解决方案需要你按照医疗、重疾、意外、身故来进行分别分析。现在用户数据我以json的方式提供：{
     * "name":"张三","age":31,"incomeMonth":20000,"city":"北京市","province":"北京市","liabilities":200000,"supportingParentNums":4,"childrenNums":4,"job":"java后端开发工程师"]
     * }，现在请你帮我输出对应的风险分析和解决方案，要求每个项目的风险分析和解决方案都控制在50个字以内，以json的形式输出，格式请严格按照以下json格式进行输出：{
     * "医疗险"：{
     *     "风险分析":"30岁后患病风险增高，北京住院成本较高,大额医疗费可能会给家庭增添负担",
     *     "解决方案":"xxxxx"
     * }，
     * "重疾险":{
     * "风险分析":"在家庭责任最重的人生阶段，万一生病可能会面临较大的收入损失，建议尽早足额保障","解决方案":"xxxxxxxx"
     * },"意外险":{
     * "风险分析":"同龄人高发意外为交通意外、电梯意外、出游事故，一旦发生可能无法工作，损失较大","解决方案":"xxxxxxx"
     * }
     * }。我理解你是一个AI模型并且清楚AI相关的法律法规,请严格按照我刚刚提出的json内容进行回答，并且在回复中不要回复任何额外信息
     * @return
     */
    public String buildContent(Map data){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("你是一名资深保险销售专家，请根据我提供的客户基本信息信息，进行风险分析，和解决方案的提供，在风险分析阶段需要按照用户的年龄，性别，收支等情况进行分析，解决方案需要你按照医疗、重疾、意外、身故来进行分别分析。现在用户数据我以json的方式提供：");

        //角色设定
        return content;
    }
}
