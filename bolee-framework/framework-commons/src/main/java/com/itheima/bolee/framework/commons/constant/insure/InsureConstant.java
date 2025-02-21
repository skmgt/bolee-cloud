package com.itheima.bolee.framework.commons.constant.insure;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.exception.ProjectException;

import java.util.Arrays;
import java.util.List;

/**
 * BUY_MODE_1onstant
 *
 * @author: wgl
 * @describe: 保险常量
 * @date: 2022/12/28 10:10
 */
public class InsureConstant extends SuperConstant{

    //常量:首页展示
    public static final String SHOW_INDEX_STATE_0 = "0";

    //常量:首页不展示
    public static final String SHOW_INDEX_STATE_1 = "1";

    //前端约定 金牌保险传值为0
    public static final String GOLD_INSURE_FRONT = "0";

    //前端约定 安心赔保险传值为1
    public static final String RELIEVED_INSURE_FRONT = "1";

    //金牌保险（是0 否1）
    public static final String IS_GOLD_INSURE_SERVER = "0";

    //安心赔（是0 否1）
    public static final String IS_RELIEVED_INSURE_SERVER = "0";

    //是否有问题（是0 否1）
    public static final String IS_PROBLEM_0 = "0";


    //校验规则：0医疗 1重疾 2意外 3养老 4年金 5旅游 6宠物 7定寿
    public static final String CHECK_RULE_0  = "0";
    public static final String CHECK_RULE_1  = "1";
    public static final String CHECK_RULE_2  = "2";
    public static final String CHECK_RULE_3  = "3";
    public static final String CHECK_RULE_4  = "4";
    public static final String CHECK_RULE_5  = "5";
    public static final String CHECK_RULE_6  = "6";
    public static final String CHECK_RULE_7  = "7";

    /**
     * 根据保险分类id获取 分类名称
     * @param typeId
     * @return
     */
    public static String getRuleNameById(String typeId) {
        switch (typeId){
            case CHECK_RULE_0:
                return "医疗";
            case CHECK_RULE_1:
                return "重疾";
            case CHECK_RULE_2:
                return "意外";
            case CHECK_RULE_3:
                return "养老";
            case CHECK_RULE_4:
                return "储蓄";
            case CHECK_RULE_5:
                return "旅游";
            case CHECK_RULE_6:
                return "宠物";
            case CHECK_RULE_7:
                return "定寿";
            default:
                throw new ProjectException();
        }
    }

    /**
     * 获取所有的保险类型
     * @return
     */
    public static List<String> getAllCheckRule() {
        return Arrays.asList(CHECK_RULE_0,CHECK_RULE_1,CHECK_RULE_2,CHECK_RULE_3,CHECK_RULE_4,CHECK_RULE_5,CHECK_RULE_6,CHECK_RULE_7);
    }

    /**
     * 热搜榜：人种榜类型为0
     */
    private final static String HOT_INSURANCE_TYPE_PERSON = "0";

    /**
     * 热搜榜：险种榜类型为1
      */
    private final static String HOT_INSURANCE_TYPE_INSURE = "1";

    /**
     * 判断是否人种榜，险种榜
     * @param type
     * @return
     */
    public static boolean checkIsInsureType(String type) {
        return HOT_INSURANCE_TYPE_INSURE.equals(type);
    }


    /**
     * 判断是否是理财险
     * @param checkRule
     * @return
     */
    public static boolean checkIsMoneyInsurance(String checkRule) {
        switch (checkRule){
            case CHECK_RULE_3:
                return true;
            case CHECK_RULE_4:
                return true;
            default:
                return false;
        }
    }
}
