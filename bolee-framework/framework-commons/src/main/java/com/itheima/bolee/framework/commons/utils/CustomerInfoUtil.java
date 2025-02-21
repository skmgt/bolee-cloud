package com.itheima.bolee.framework.commons.utils;

import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CustomerInfoUtil
 *
 * @author: wgl
 * @describe: 用户信息工具类
 * @date: 2022/12/28 10:10
 */
public class CustomerInfoUtil {

    /**
     * 根据身份证号获取年龄
     * @param idNumber 身份证号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idNumber) {
        int age = 0;
        if (idNumber.length() == 18) {
            String birthDateStr = idNumber.substring(6, 14); // 提取出生日期部分
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                Date birthDate = sdf.parse(birthDateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(birthDate);
                int birthYear = cal.get(Calendar.YEAR);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                age = currentYear - birthYear;
            } catch (ParseException e) {
                // 处理日期解析异常
                e.printStackTrace();
            }
        }
        return age;
    }

    /**
     * 根据身份证号获取出生日期
     * @param idNumber 身份证号
     * @return 出生日期字符串
     */
    public static String getBirthDateByIdCard(String idNumber) {
        String birthDateStr = "";
        if (idNumber.length() == 18) {
            birthDateStr = idNumber.substring(6, 14); // 提取出生日期部分
        }
        return birthDateStr;
    }

    /**
     * 根据身份证号获取性别
     * @return
     */
    public static String setSexByIdCard(String idNumber) {
        if(StrUtil.isNotEmpty(idNumber)) {
            if (idNumber.length() == 18) {
                char genderChar = idNumber.charAt(16); // 18位身份证号的第17位
                int genderDigit = Character.getNumericValue(genderChar);
                if (genderDigit % 2 == 0) {
                    return "女";
                } else {
                    return "男";
                }
            } else if (idNumber.length() == 15) {
                char genderChar = idNumber.charAt(14); // 15位身份证号的最后一位
                int genderDigit = Character.getNumericValue(genderChar);
                if (genderDigit % 2 == 0) {
                    return "女";
                } else {
                    return "男";
                }
            } else {
                // 如果身份证号位数不是15位或18位，可以根据实际需求返回错误信息或其他处理方式
                return "无法确定性别";
            }
        }else {
            // 如果身份证号位数不是15位或18位，可以根据实际需求返回错误信息或其他处理方式
            return "无法确定性别";
        }
    }
}
