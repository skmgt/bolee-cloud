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
/**
 * @Description：风险类目
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RiskAnalysis对象", description="风险类目")
public class RiskAnalysisVO extends BaseVO {

    @Builder
    public RiskAnalysisVO(Long id,String dataState,Long insuranceId,String assessmentKey,String assessmentKeyName,String assessmentVal,String assessmentType,Integer sortNo,String remake){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.assessmentKey=assessmentKey;
        this.assessmentKeyName=assessmentKeyName;
        this.assessmentVal=assessmentVal;
        this.assessmentType=assessmentType;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "保险产品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "风险项key")
    private String assessmentKey;

    @ApiModelProperty(value = "风险项项名称")
    private String assessmentKeyName;

    @ApiModelProperty(value = "风险项项值")
    private String assessmentVal;

    @ApiModelProperty(value = "风险项类型: 0医疗风险 1重疾风险 2身故风险 3意外风险")
    private String assessmentType;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
