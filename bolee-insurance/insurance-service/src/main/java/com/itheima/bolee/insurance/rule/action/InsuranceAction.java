package com.itheima.bolee.insurance.rule.action;



import com.itheima.bolee.framework.rule.action.ActionId;
import com.itheima.bolee.framework.rule.model.ExposeAction;
import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethodParameter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * InsuranceAction
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Slf4j
public class InsuranceAction {


    @ActionId("helloKey")
    @ActionMethodParameter(names={"用户ID","key","value"})
    public String hello(String username){
        log.info("hello "+username);
        return "hello"+username;
    }
}