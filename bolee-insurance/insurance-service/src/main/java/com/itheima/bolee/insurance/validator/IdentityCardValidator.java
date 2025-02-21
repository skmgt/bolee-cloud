package com.itheima.bolee.insurance.validator;

/**
 * IdentityCardValidator
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
import com.itheima.bolee.insurance.validator.anno.IdentityCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class IdentityCardValidator implements ConstraintValidator<IdentityCard, String> {

    @Override
    public void initialize(IdentityCard constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许为空
        }
        int length = value.length();
        if (length != 15 && length != 18) {
            return false; // 身份证号位数必须为15位或18位
        }
        // 检查年月日是否合法，根据具体需求实现
        if (!isValidDate(value)) {
            return false;
        }
        return true;
    }

    /**
     * 实现日期验证逻辑，需要根据具体身份证号规范来编写
      */
    private boolean isValidDate(String value) {
        if (value.length() == 15) {
            // 15位身份证号的年份使用19XX格式
            String year = "19" + value.substring(6, 8);
            String month = value.substring(8, 10);
            String day = value.substring(10, 12);

            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            int dayInt = Integer.parseInt(day);

            // 获取当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            String currentDateString = sdf.format(currentDate);
            int currentYear = Integer.parseInt(currentDateString.substring(0, 4));
            int currentMonth = Integer.parseInt(currentDateString.substring(4, 6));
            int currentDay = Integer.parseInt(currentDateString.substring(6, 8));

            if (yearInt < 1900 || yearInt > currentYear) {
                return false; // 年份不合法
            }
            if (yearInt == currentYear && monthInt > currentMonth) {
                return false; // 月份不合法
            }
            if (yearInt == currentYear && monthInt == currentMonth && dayInt > currentDay) {
                return false; // 日期不合法
            }
        } else if (value.length() == 18) {
            // 18位身份证号的年份使用20XX格式
            String year = value.substring(6, 10);
            String month = value.substring(10, 12);
            String day = value.substring(12, 14);

            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            int dayInt = Integer.parseInt(day);

            // 获取当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            String currentDateString = sdf.format(currentDate);
            int currentYear = Integer.parseInt(currentDateString.substring(0, 4));
            int currentMonth = Integer.parseInt(currentDateString.substring(4, 6));
            int currentDay = Integer.parseInt(currentDateString.substring(6, 8));

            if (yearInt < 1900 || yearInt > currentYear) {
                return false; // 年份不合法
            }
            if (yearInt == currentYear && monthInt > currentMonth) {
                return false; // 月份不合法
            }
            if (yearInt == currentYear && monthInt == currentMonth && dayInt > currentDay) {
                return false; // 日期不合法
            }
        } else {
            return false; // 非法位数
        }
        return true; // 日期合法
    }
}
