package com.itheima.bolee.insurance.urule.action;

import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionBean;
import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethod;
import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethodParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SickLogAction
 *
 * @author: wgl
 * @describe: 动作库：疾病日志
 * @date: 2022/12/28 10:10
 */
@ActionBean(name = "疾病日志")
@Component
@Slf4j
public class SickLogAction {


    @ActionMethod(name = "记录规审核疾病")
    @ActionMethodParameter(names = {"疾病列表"})
    public void saveSickLog(List sicks) {
        log.info("当前疾病为" +sicks);
    }
}