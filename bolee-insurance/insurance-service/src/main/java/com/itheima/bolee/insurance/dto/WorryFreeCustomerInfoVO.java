package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.rule.model.Label;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * WorryFreeCustomerInfoDTO
 *
 *{income=6000, cityNo=1, childNum=1, idCard=430203199206251018, sex=男, seriousScore=0, eduNo=3, dieScore=0, expensesMonth=5000, applicationAmount=250000, this$0=com.itheima.bolee.insurance.service.impl.RuleServiceImpl@50dc169c, jobNo=JOB_01_02, name=张三, liabilities=25000, checkList=[], deposit=20000, accidentScore=0, riskItem=[], id=1234564878, repaymentCycle=2, medicalScore=0, age=21}
 *         map.put("jobNo","JOB_01_02");
 *         map.put("riskItem",new ArrayList<>());
 * @author: wgl
 * @describe: 省心配省心配对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="省心配数据传输对象", description="省心配传输对象")
public class WorryFreeCustomerInfoVO {

    @Label("用户id")
    @ApiModelProperty(value = "用户id")
    private String id;

    @Label("客户编号")
    @ApiModelProperty(value = "申请人姓名")
    private String name;

    @Label("年龄")
    @ApiModelProperty(value = "年龄")
    private int age;

    @Label("性别")
    @ApiModelProperty(value = "性别")
    private String sex;

    @Label("职业")
    @ApiModelProperty(value = "职业")
    private String jobNo;

    @Label("身份证号")
    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @Label("每月收入")
    @ApiModelProperty(value = "每月收入")
    private BigDecimal income;

    @Label("子女数量")
    @ApiModelProperty(value = "子女数量")
    private int childNum;

    @Label("赡养父母数量")
    @ApiModelProperty(value = "赡养父母数量")
    private int parentNum;

    @Label("常驻城市")
    @ApiModelProperty(value = "常驻城市")
    private String cityNo;

    @Label("医疗风险总分")
    @ApiModelProperty(value = "医疗风险总分")
    private Integer medicalScore=0;

    @Label("意外风险总分")
    @ApiModelProperty(value = "意外风险总分")
    private Integer accidentScore=0;

    @Label("身故风险总分")
    @ApiModelProperty(value = "身故风险总分")
    private Integer dieScore=0;

    @Label("重疾风险总分")
    @ApiModelProperty(value = "重疾风险总分")
    private Integer seriousScore=0;

    @Label("风险项结果")
    @ApiModelProperty(value = "风险项结果")
    private List riskItem = new ArrayList();

    @Label("风险结果")
    @ApiModelProperty(value = "风险结果")
    private List risks = new ArrayList();

    @Label("保障项列表")
    private List safeguardList = new ArrayList();

    @Label("医疗保额")
    private BigDecimal medicalAmount = BigDecimal.ZERO;

    @Label("意外保额")
    private BigDecimal accidentAmount = BigDecimal.ZERO;

    @Label("身故保额")
    private BigDecimal dieAmount = BigDecimal.ZERO;

    @Label("重疾保额")
    private BigDecimal seriousAmount = BigDecimal.ZERO;

    @Label("医疗配额级别")
    private String medicalLevel;

    @Label("意外配额级别")
    private String accidentLevelt;

    @Label("身故配额级别")
    private String dieLevel;

    @Label("重疾配额级别")
    private String seriousLevel;

    @Label("用户标签")
    private List userTag = new ArrayList();
}
