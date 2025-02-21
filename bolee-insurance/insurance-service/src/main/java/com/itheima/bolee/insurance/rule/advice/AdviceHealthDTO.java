package com.itheima.bolee.insurance.rule.advice;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AdviceHealthDTO
 *
 * @author: wgl
 * @describe: 健康检查传输对象
 * @date: 2022/12/28 10:10
 */
@Data
public class AdviceHealthDTO {

    @Label("保险")
    private String insuranceId;

    @Label("用户id")
    private String userId;

    @Label("疾病列表")
    private List<String> sickKey = new ArrayList<>();

    //key 问卷唯一标识 value 用户提交选项内容
    @Label("问卷列表")
    private Map questions;

    @Label("问卷结果")
    private List<String> sicks = new ArrayList<>();

    @Label("初筛结果")
    private Boolean result = true;

}
