package com.itheima.bolee.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description：保险产品
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Insurance对象", description = "保险产品")
public class InsuranceVO extends BaseVO {

    @Builder
    public InsuranceVO(BigDecimal operatingRate, BigDecimal individualAgentRate, BigDecimal platformAgentRate, String goldSelection, String carefree, Long id, String dataState, List<FileVO> fileVOs, String publishNumber, Long categoryNo, Long recommendCategoryNo, String insuranceName, String showIndex, String labelsJson, String remakeJson, Long timeStart, String timeStartUnit, Long timeEnd, String timeEndUnit, String relation, Integer sortNo, String multiple, Long continuousInsuranceAge, String checkRule, String companyNo, String insuranceState, Integer grace, String graceUnit, Integer revival, String revivalUnit, String comment, String remake, Integer hesitation, Integer waits,String[] checkedIds) {
        super(id, dataState);
        this.publishNumber = publishNumber;
        this.categoryNo = categoryNo;
        this.recommendCategoryNo = recommendCategoryNo;
        this.insuranceName = insuranceName;
        this.goldSelection = goldSelection;
        this.carefree = carefree;
        this.showIndex = showIndex;
        this.labelsJson = labelsJson;
        this.remakeJson = remakeJson;
        this.timeStart = timeStart;
        this.timeStartUnit = timeStartUnit;
        this.timeEnd = timeEnd;
        this.timeEndUnit = timeEndUnit;
        this.relation = relation;
        this.sortNo = sortNo;
        this.multiple = multiple;
        this.continuousInsuranceAge = continuousInsuranceAge;
        this.checkRule = checkRule;
        this.companyNo = companyNo;
        this.insuranceState = insuranceState;
        this.grace = grace;
        this.graceUnit = graceUnit;
        this.revival = revival;
        this.revivalUnit = revivalUnit;
        this.comment = comment;
        this.remake = remake;
        this.hesitation = hesitation;
        this.waits = waits;
        this.fileVOs = fileVOs;
        this.operatingRate = operatingRate;
        this.individualAgentRate = individualAgentRate;
        this.platformAgentRate = platformAgentRate;
        this.checkedIds = checkedIds;
    }

    @ApiModelProperty(value = "银保监备案号")
    private String publishNumber;

    @ApiModelProperty(value = "分类编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryNo;

    @ApiModelProperty(value = "推荐分类")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recommendCategoryNo;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "金选：0是 1否")
    private String goldSelection;

    @ApiModelProperty(value = "安心赔：0是 1否")
    private String carefree;

    @ApiModelProperty(value = "首页热点显示（是0 否1）")
    private String showIndex;

    @ApiModelProperty(value = "保险标签格式：[{key:200万医疗金，val:指定私立意义}]")
    private String labelsJson;

    @ApiModelProperty(value = "补充说明格式[{key:失去保障，val:断保将失去相应保障，不能理赔}]")
    private String remakeJson;

    @ApiModelProperty(value = "可购买起始点")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long timeStart;

    @ApiModelProperty(value = "可购买起始点单位：天、年")
    private String timeStartUnit;

    @ApiModelProperty(value = "可购买结束点")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long timeEnd;

    @ApiModelProperty(value = "可购买结束点单位：天、年")
    private String timeEndUnit;

    @ApiModelProperty(value = "可投保关系：self,children:spouse,parents")
    private String relation;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "团个险（0团 1个）")
    private String multiple;

    @ApiModelProperty(value = "连续投保年龄")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long continuousInsuranceAge;

    @ApiModelProperty(value = "校验规则：0医疗 1重疾 2意外 3养老 4年金 5旅游 6宠物 7定寿")
    private String checkRule;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "保险状态（上架0 下架1）")
    private String insuranceState;

    @ApiModelProperty(value = "保单宽限")
    private Integer grace;

    @ApiModelProperty(value = "宽限单位")
    private String graceUnit;

    @ApiModelProperty(value = "保单复效")
    private Integer revival;

    @ApiModelProperty(value = "复效单位")
    private String revivalUnit;

    @ApiModelProperty(value = "产品点评")
    private String comment;

    @ApiModelProperty(value = "产品描述")
    private String remake;

    @ApiModelProperty(value = "犹豫期")
    private Integer hesitation;

    @ApiModelProperty(value = "等待期")
    private Integer waits;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "运营费率")
    private BigDecimal operatingRate;

    @ApiModelProperty(value = "个人代理费率")
    private BigDecimal individualAgentRate;

    @ApiModelProperty(value = "平台代理费率")
    private BigDecimal platformAgentRate;

    @ApiModelProperty(value = "保险方案")
    private List<InsurancePlanVO> insurancePlanVOs;

    @ApiModelProperty(value = "公司信息")
    private CompanyVO companyVO;

    @ApiModelProperty(value = "保险系数项")
    private List<InsuranceCoefficentVO> insuranceCoefficentVOs;

    @ApiModelProperty(value = "查询：保险筛选项值")
    private List<String> conditionVals;

    @ApiModelProperty(value = "保险筛选项")
    private List<InsuranceConditionVO> insuranceConditionVOs;

}
