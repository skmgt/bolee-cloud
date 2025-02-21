package com.itheima.bolee.framework.commons.utils;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 构造ResponseResult工具
 */
public class ResponseResultBuild {

    public static <T> ResponseResult<T> build(IBaseEnum basicEnumIntface, T t) {
        //从当前线程获得用户对象
        UserVO userVO = SubjectContent.getUserVO();
        if (!EmptyUtil.isNullOrEmpty(userVO)) {
            //构建对象
            return ResponseResult.<T>builder()
                    .code(basicEnumIntface.getCode())
                    .msg(basicEnumIntface.getMsg())
                    .data(t)
                    .operatorId(userVO.getId())
                    .operatorName(userVO.getUsername())
                    .operationTime(LocalDateTime.now())
                    .operatorSex(userVO.getSex())
                    ._class(getClassName(t))
                    .tip("本站点所有接口仅用于教学演示，所有数据均非真实数据，请勿用作其它用途！")
                    .build();
        }
        //构建对象
        return ResponseResult.<T>builder()
                .code(basicEnumIntface.getCode())
                .msg(basicEnumIntface.getMsg())
                .data(t)
                .operationTime(LocalDateTime.now())
                .build();
    }

    /**
     * 公共成功响应方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> successBuild(T t) {
        return ResponseResultBuild.build(BaseEnum.SUCCEED, t);
    }

    /**
     * 公共失败响应方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> failedBuild(T t) {
        return ResponseResultBuild.build(BaseEnum.SYSYTEM_FAIL, t);
    }

    /**
     * 公共失败响应方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> validateErrorBuild(T t) {
        return ResponseResultBuild.build(BaseEnum.VALID_EXCEPTION, t);
    }


    private static String getClassName(Object obj) {
        if (obj == null) {
            return "null";
        } else {
            Class<?> clazz = obj.getClass();
            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                if (list.isEmpty()) {
                    return "List<Empty>";
                } else {
                    Object firstElement = list.get(0);
                    return "List<" + firstElement.getClass().getName() + ">";
                }
            } else {
                return clazz.getName();
            }
        }
    }
}
