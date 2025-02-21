package com.itheima.bolee.insurance.constant;

/**
 * RuleConstant
 *
 * @author: wgl
 * @describe: 规则常量类
 * @date: 2022/12/28 10:10
 */
public class RuleConstant {

    public final static String RULE_GROUP_NAME = "四方保险销售平台";

    private final static String SICK_ADVICE_KNOWLEDGEID = "rule_stream_34";

    private final static String SICK_ADVICE_PROCESSID = "rule_stream_01";

    public final static String SLASH = "/";

    /**
     * 获取
     * @param knowledgeId
     * @return
     */
    public final static String getRuleKnowledge(String knowledgeId){
        return RULE_GROUP_NAME+SLASH+knowledgeId;
    }

    /**
     * 获取
     * @return
     */
    public final static String sickAdviceAdviceKnowledge(){
        return RULE_GROUP_NAME+SLASH+SICK_ADVICE_KNOWLEDGEID;
    }

    /**
     * 获取
     * @return
     */
    public final static String sickAdviceProcessId(){
        return SICK_ADVICE_PROCESSID;
    }
}
