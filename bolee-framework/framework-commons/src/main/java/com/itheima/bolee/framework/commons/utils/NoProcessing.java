package com.itheima.bolee.framework.commons.utils;

/**
 * @ClassName NoProcessing.java
 * @Description 编号创建类
 */
public class NoProcessing {

    public static void main(String[] args) {
//        String input = "100001009001001";
//        String processedString = createNo(input,true);
//        System.out.println(processedString);

        String sonNo = "100001001001000";
        String parentNo = "100001001000000";

        System.out.println(isParent(parentNo,sonNo));
    }

    /***
     * @description 处理补全编号，所有编号都是15位，共5层，每层关系如下：
     * 第一层：100000000000000
     * 第二层：100001000000000
     * 第三层：100001001000000
     * 第四层：100001001001000
     * 第五层：100001001001001
     * 处理后100000000000000后结果为100
     * @param input
     * @return: java.lang.String
     */
    public static String processString(String input) {
        int step = input.length() / 3;
        for (int i =0;i<step;i++ ){
            String targetString = input.substring(input.length()-3,input.length());
            if ("000".equals(targetString)){
                input = input.substring(0,input.length()-3);
            }else {
                break;
            }
        }
        return input;
    }

    /***
     * @description 生产层级编号
     * @param input 输入编号
     * @param peerNode 是否有下属节点
     * @return
     * @return: java.lang.String
     */
    public static String createNo(String input,boolean peerNode) {
        int step = input.length() / 3;
        int supplement = 0;
        for (int i =0;i<step;i++ ){
            String targetString = input.substring(input.length()-3,input.length());
            if ("000".equals(targetString)){
                input = input.substring(0,input.length()-3);
                supplement++;
            }else {
                break;
            }
        }
        if (peerNode){
            input = String.valueOf(Long.valueOf(input) + 1L);
            for (int i =0;i<supplement;i++ ){
                input = input+"000";
            }
        }else {
            input = String.valueOf(Long.valueOf(input+"001"));
            for (int i =0;i<supplement-1;i++ ){
                input = input+"000";
            }
        }
        return input;
    }

    /***
     * @description 是否是其子类
     *
     * @param parentNo 父编号
     * @param sonNo 子编号
     * @return
 * @return: java.lang.Boolean
     */
    public static Boolean isParent(String parentNo,String sonNo) {
        parentNo = processString(parentNo);
        return sonNo.startsWith(parentNo);
    }

}
