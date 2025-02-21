package com.itheima.bolee.framework.aspect;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @ClassName SensitiveAspect.java
 * @Description TODO
 */
@Aspect
@Component
public class SensitiveAspect {

    @Around(value = "@annotation(sensitiveResponse)")
    public Object select(ProceedingJoinPoint  joinPoint, SensitiveResponse sensitiveResponse) throws Throwable {
        // 执行目标方法
        Object result = joinPoint.proceed();
        // 获取目标方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取目标方法的泛型返回类型
        Type genericReturnType = method.getGenericReturnType();
        if (!EmptyUtil.isNullOrEmpty(result)){
            // 进行脱敏处理
            String jsonResponse = JSONObject.toJSONString(result);
            String maskedResponse = maskSensitiveData(jsonResponse);
            // 将脱敏后的数据转换为实际返回类型
            return JSONObject.parseObject(maskedResponse,genericReturnType);
        }
        return result;
    }



    private String maskSensitiveData(String jsonData) {
        //使用正则表单式替换，我也不会写~~直接GPT搞定！
        return jsonData
            .replaceAll("\"identityCard\"\\s*:\\s*\"(\\d{1})(\\d{15})(\\d{2})\"", "\"identityCard\":\"$1**************$3\"")
            .replaceAll("\"name\"\\s*:\\s*\"(.).+?\"", "\"name\":\"$1*\"")
            .replaceAll("\"bankCardNo\"\\s*:\\s*\"(.).+?\"", "\"bankCardNo\":\"$1*\"")
            .replaceAll("\"bankName\"\\s*:\\s*\"(.).+?\"", "\"bankName\":\"$1*\"")
            .replaceAll("\"bankReservedPhoneNum\"\\s*:\\s*\"(.).+?\"", "\"bankReservedPhoneNum\":\"$1*\"");
    }
}
