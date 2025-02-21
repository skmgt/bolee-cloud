package com.itheima.bolee.framework.commons.constant.warranty;

/**
 * WarrantyConstant
 *
 * @author: wgl
 * @describe: 保险常量
 * @date: 2022/12/28 10:10
 */
public class WarrantyConstant {

    //0待付款
    public final static String STATE_NOT_PAY = "0";
    //1待生效
    public final static String STATE_TO_BE_WORK = "1";
    //2保障中
    public final static String STATE_SAFEING= "2";
    //3 逾期中止
    public final static String STATE_OVERDUE_OVER = "3";
    //4 理赔终止
    public final static String STATE_SETTLEMENT_OVER = "4";
    //5 复效中止
    public final static String STATE_REINSTATE_ING_OVER = "5";
    //6 复效终止
    public final static String STATE_REINSTATE_OVER = "6";
    //7 满期终止
    public final static String STATE_EXPIRE_OVER = "7";
    //8 拒保
    public final static String STATE_DECLINATURE = "8";
    //9 犹豫期退保
    public final static String STATE_MISS_REFUND = "9";
    //10 协议退保
    public final static String STATE_ARGEE_REFUND = "10";

    //核保状态(0发送失败 1核保中 2核保失败 3核保成功 )
    public final static String UNDERWRITING_STATE_0="0";
    public final static String UNDERWRITING_STATE_1="1";
    public final static String UNDERWRITING_STATE_2="2";
    public final static String UNDERWRITING_STATE_3="3";

    //批保状态(0未批保 1批保发送失败 2批保中 3批保通过 4.保批不通过)
    public final static String APPROVE_STATE_UN_SEND="0";
    public final static String APPROVE_STATE_SEND_ERROR="1";
    public final static String APPROVE_STATE_APPROVEING="2";
    public final static String APPROVE_STATE_APPROVE="3";
    public final static String APPROVE_STATE_REFUSE="4";

    public final static String APPROVE_TYPE_APPOVE="0";

    public final static String APPROVE_TYPE_INSURERD="1";

    /**
     * 判断是否是被保人
     * @param type
     * @return
     */
    public static boolean isInsured(String type) {
        return APPROVE_TYPE_INSURERD.equals(type);
    }
}
