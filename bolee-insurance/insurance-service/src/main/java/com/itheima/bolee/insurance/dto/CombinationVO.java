package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itheima.bolee.framework.commons.dto.file.FileVO;
/**
 * @Description：组合方案
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Combination对象", description="组合方案")
public class CombinationVO extends BaseVO {


    @Builder
    public CombinationVO(Long id, String dataState, String combinationName, String riskAnalysis, String riskScenario, List<FileVO> fileVOs, String[] checkedIds, CombinationVO combination, List<InsuranceVO> insuranceList, String totalMoney) {
        super(id, dataState);
        this.combinationName = combinationName;
        this.riskAnalysis = riskAnalysis;
        this.riskScenario = riskScenario;
        this.fileVOs = fileVOs;
        this.checkedIds = checkedIds;
        this.combination = combination;
        this.insuranceList = insuranceList;
        this.totalMoney = totalMoney;
    }

    @ApiModelProperty(value = "组合名称")
    private String combinationName;

    @ApiModelProperty(value = "风险分析")
    private String riskAnalysis;

    @ApiModelProperty(value = "主要分析场景")
    private String riskScenario;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "保险方案")
    private CombinationVO combination;

    @ApiModelProperty(value = "关联的保险列表")
    private List<InsuranceVO> insuranceList;

    @ApiModelProperty(value = "方案金额")
    private String totalMoney;

    @ApiModelProperty(value = "批量操作：保险id")
    private String[] checkIds;
}
