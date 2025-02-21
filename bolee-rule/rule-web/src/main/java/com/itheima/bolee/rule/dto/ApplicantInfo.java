package com.itheima.bolee.rule.dto;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ApplicantInfo
 *
 * @author: wgl
 * @describe: 申请人信息
 * @date: 2022/12/28 10:10
 */
@Data
public class ApplicantInfo {

    @Label("用户id")
    private String id;

    @Label("申请人姓名")
    private String name;

    @Label("身份证号")
    private String idCard;

    @Label("年龄")
    private int age = 0;

    @Label("收入")
    private BigDecimal income = BigDecimal.ZERO;

    @Label("职业")
    private String jobNo;

    @Label("常驻城市")
    private String cityNo;

    @Label("子女数量")
    private int childNum;

    @Label("赡养父母数量")
    private int supportingParents;

    @Label("风险结果")
    private List<String> risks;

    @Label("风险项结果")
    private List<String> riskItem;
}