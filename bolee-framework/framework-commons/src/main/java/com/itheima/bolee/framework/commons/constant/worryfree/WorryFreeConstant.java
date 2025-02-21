package com.itheima.bolee.framework.commons.constant.worryfree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * WorryFreeConstant
 *
 * @author: wgl
 * @describe: 省心配常量
 * @date: 2022/12/28 10:10
 */
public class WorryFreeConstant {

    //省心配风险redis中的key
    public static final String WORRYFREE_PREFIX = "worryfree:";

    public static final String FLOW_NAME_RISK_START = "风险分析开始";

    public static final String FLOW_NAME_RISK_END = "风险分析结束";

    public static final String FLOW_NAME_SAFEGUARD_ANALYSIS_START = "保障评估开始";
    public static final String FLOW_NAME_SAFEGUARD_ANALYSIS_END = "保障评估结束";

    public static final String FLOW_NAME_PRODUCT_MATCH_START = "产品匹配开始";

    public static final String FLOW_NAME_PRODUCT_MATCH_ING = "产品匹配";
    public static final String FLOW_NAME_PRODUCT_MATCH_END = "产品匹配结束";

    /**
     * 风险项医疗
     */
    public static final String RISK_TYPE_MEDICAL = "medical";

    /**
     * 风险项意外
     */
    public static final String RISK_TYPE_ACCIDENT = "accident";

    /**
     * 风险项身故
     */
    public static final String RISK_TYPE_DIE = "die";

    /**
     * 用户标签
     */
    public static final String USER_TAG = "userTag";

    /**
     * 风险项重疾
     */
    public static final String RISK_TYPE_SERIOUS = "serious";


    /**
     * 风险项类型
     */
    public static final String VARIABLE_KEY_SAFEGUARD = "safeguardList";

    /**
     * 医疗变量键
     */
    public static final String VARIABLE_KEY_MEDICAL_AMOUNT = "medicalAmount";

    /**
     * 意外变量键
     */
    public static final String VARIABLE_KEY_ACCIDENT_AMOUNT = "accidentAmount";

    /**
     * 身故变量键
     */
    public static final String VARIABLE_KEY_DIE_AMOUNT = "dieAmount";

    /**
     * 重疾变量键
     */
    public static final String VARIABLE_KEY_SERIOUS_AMOUNT = "seriousAmount";

    /**
     * 医疗分类编号
     */
    public static final String CATEGORY_NO_MEDICAL = "100001001000000";


    /**
     * 重疾分类编号
     */
    public static final String CATEGORY_NO_SERIOUS = "100001002000000";

    /**
     * 意外分类编号
     */
    public static final String CATEGORY_NO_ACCIDENT = "100001003000000";



    /**
     * 获取所有的风险项类型
     * @return
     */
    public static List<String> getAllRiskTypeList() {
        List<String> nodeNameList = Arrays.asList(RISK_TYPE_MEDICAL, RISK_TYPE_ACCIDENT, RISK_TYPE_DIE, RISK_TYPE_SERIOUS);
        return nodeNameList;
    }

    /**
     * 风险分析流程
     * @return
     */
    public static List<String> getRiskFlowList() {
        List<String> nodeNameList = Arrays.asList("风险分析流程开始", "用户数据构建", "三方数据调用", "风险分析计算中", "准入分析", "反欺诈分析", "风险导入","风险打分");
        return nodeNameList;
    }

    /**
     * 保障配额流程节点
     * @return
     */
    public static List<String> getSafeguardFlowList() {
        List<String> nodeNameList = Arrays.asList("评估保障开始", "保障项数据获取", "三方数据调用", "评估保障计算中", "个人收入项验证", "收入反欺诈分析", "评估保障结束");
        return nodeNameList;
    }

    /**
     * 产品匹配节点列表
     * @return
     */
    public static List<String> getProductMatchFlowList() {
        List<String> nodeNameList = Arrays.asList("产品匹配开始","系统保险数据获取", "保险计算", "保额计算", "保额数据获取", "保额最大努力匹配", "保险匹配");
        return nodeNameList;
    }

    /**
     * 产品匹配节点列表
     * @return
     */
    public static List<ProductTypeDTO> getProductTypeCategoryList() {
        List<ProductTypeDTO> nodeNameList = Arrays.asList(
                ProductTypeDTO.builder().categoryNo(CATEGORY_NO_MEDICAL).categoryKey(VARIABLE_KEY_MEDICAL_AMOUNT).build(),
                ProductTypeDTO.builder().categoryNo(CATEGORY_NO_SERIOUS).categoryKey(VARIABLE_KEY_SERIOUS_AMOUNT).build(),
                ProductTypeDTO.builder().categoryNo(CATEGORY_NO_ACCIDENT).categoryKey(VARIABLE_KEY_ACCIDENT_AMOUNT).build()
        );
        return nodeNameList;
    }

    /**
     * 获取锁key
     * @param id
     * @return
     */
    public static String getLockKey(String id) {
        //省心配风险redis中的key
        return WORRYFREE_PREFIX + "worryfree:key:"+id;
    }
}