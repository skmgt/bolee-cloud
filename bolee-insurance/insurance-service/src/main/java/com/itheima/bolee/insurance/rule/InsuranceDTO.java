package com.itheima.bolee.insurance.rule;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

/**
 * InsuranceDTO
 *
 * @author: wgl
 * @describe: 保险基本信息
 * @date: 2022/12/28 10:10
 */
@Data
public class InsuranceDTO {

    @Label(value = "银保监备案号")
    private String publishNumber;

    @Label(value = "分类编号")
    private Long categoryNo;

    @Label(value = "推荐分类")
    private String recommendCategoryNo;

    @Label(value = "保险名称")
    private String insuranceName;

    @Label(value = "保险标签  0金选 1安心培 2普通")
    private String insuranceLabel;

    @Label(value = "首页热点显示（是0 否1）")
    private String showIndex;

    @Label(value = "保险标签格式：[{key:200万医疗金，val:指定私立意义}]")
    private String labelsJson;

    @Label(value = "补充说明格式[{key:失去保障，val:断保将失去相应保障，不能理赔}]")
    private String remakeJson;

    @Label(value = "可购买起始点")
    private Integer timeStart;

    @Label(value = "可购买起始点单位：天、年")
    private String timeStartUnit;

    @Label(value = "可购买结束点")
    private Integer timeEnd;

    @Label(value = "可购买结束点单位：天、年")
    private String timeEndUnit;

    @Label(value = "可投保关系：self,children:spouse,parents")
    private String relation;

    @Label(value = "排序")
    private Integer sortNo;

    @Label(value = "团个险（0团 1个）")
    private String multiple;

    @Label(value = "连续投保年龄")
    private Long continuousInsuranceAge;

    @Label(value = "企业编号")
    private String companyNo;

    @Label(value = "保险状态（上架0 下架1）")
    private String insuranceState;

    @Label(value = "保单宽限")
    private Integer grace;

    @Label(value = "宽限单位")
    private String graceUnit;

    @Label(value = "保单复效")
    private String revival;

    @Label(value = "复效单位")
    private String revivalUnit;

    @Label(value = "产品点评")
    private String comment;

    @Label(value = "产品描述")
    private String remake;

    @Label(value = "批量操作：主键ID")
    private String[] checkedIds;

    @Label(value = "校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿")
    private String checkRule;
}
