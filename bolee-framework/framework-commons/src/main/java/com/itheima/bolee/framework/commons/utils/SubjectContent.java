package com.itheima.bolee.framework.commons.utils;

import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName subjectContent.java
 * @Description 用户主体对象
 */
@Slf4j
public class SubjectContent {


    /***
     * @description 创建线程局部userVO变量
     */
    public static ThreadLocal<UserVO> userVOThreadLocal = new ThreadLocal<UserVO>() {
        @Override
        protected UserVO initialValue() {
            return null;
        }
    };

    /***
     * @description 创建线程局部dataSecurityVO变量
     */
    public static ThreadLocal<DataSecurityVO> dataSecurityVOThreadLocal = new ThreadLocal<DataSecurityVO>() {
        @Override
        protected DataSecurityVO initialValue() {
            return null;
        }
    };

    /***
     * @description 创建线程局部companyNo变量
     */
    public static ThreadLocal<String> companyNoThreadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    /***
     * @description 创建线程局部userToken变量
     */
    public static ThreadLocal<String> userTokenThreadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };



    // 提供线程局部companyNo变量set方法
    public static void setCompanyNo(String companyNo) {

        companyNoThreadLocal.set(companyNo);
    }

    // 提供线程局部dataSecurityVO变量set方法
    public static void setDataSecurityVO(DataSecurityVO dataSecurityVO) {

        dataSecurityVOThreadLocal.set(dataSecurityVO);
    }

    // 提供线程局部userVO变量set方法
    public static void setUserVO(UserVO userVO) {

        userVOThreadLocal.set(userVO);
    }
    // 提供线程局部userToken变量set方法
    public static void setUserToken(String userToken) {

        userTokenThreadLocal.set(userToken);
    }
    // 提供线程局部companyNo变量get方法
    public static String getCompanyNo() {

        return companyNoThreadLocal.get();
    }
    // 提供线程局部dataSecurityVO变量get方法
    public static DataSecurityVO getDataSecurityVO() {

        return dataSecurityVOThreadLocal.get();
    }
    // 提供线程局部userVO变量get方法
    public static UserVO getUserVO() {

        return userVOThreadLocal.get();
    }
    // 提供线程局部userToken变量get方法
    public static String getUserToken() {

        return userTokenThreadLocal.get();
    }
    //清空当前线程companyNo，防止内存溢出
    public static void removeCompanyNo() {

        companyNoThreadLocal.remove();
    }
    //清空当前线程dataSecurityVO，防止内存溢出
    public static void removeDataSecurityVO() {

        dataSecurityVOThreadLocal.remove();
    }
    //清空当前线程userVO，防止内存溢出
    public static void removeUserVO() {

        userVOThreadLocal.remove();
    }
    //清空当前线程userToken，防止内存溢出
    public static void removeUserToken() {

        userTokenThreadLocal.remove();
    }
}
