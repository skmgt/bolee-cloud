//package com.itheima.bolee.rule.action;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpUtil;
//import com.itheima.bolee.framework.rule.RuleException;
//import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionBean;
//import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethod;
//import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethodParameter;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * LogAction
// *
// * @author: wgl
// * @describe: TODO
// * @date: 2022/12/28 10:10
// */
//@ActionBean(name="日志")
//@Component
//public class LogAction {
//
//    @ActionMethod(name="记录规则执行日志")
//    @ActionMethodParameter(names={"校验结果","校验描述"})
//    public void saveLog(Boolean checkResult, String des){
//        String url = "http://127.0.0.1:7065/log/"+checkResult+"/"+des;
//        HttpRequest get = HttpUtil.createGet(url);
//        // 如果有cookie 或者token进行鉴权，可以在此添加
//        String body = get.execute().body();
//        System.out.println(body);
//    }
//}