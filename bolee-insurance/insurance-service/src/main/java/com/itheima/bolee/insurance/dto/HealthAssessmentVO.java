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
 * @Description：评估类目
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="HealthAssessment对象", description="评估类目")
public class HealthAssessmentVO extends BaseVO {

    @Builder
    public HealthAssessmentVO(Long id,String dataState,Long insuranceId,String assessmentKey,String assessmentKeyName,String assessmentVal,Integer sortNo,String remake){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.assessmentKey=assessmentKey;
        this.assessmentKeyName=assessmentKeyName;
        this.assessmentVal=assessmentVal;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "保险产品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "评估类目key")
    private String assessmentKey;

    @ApiModelProperty(value = "评估类目名称")
    private String assessmentKeyName;

    @ApiModelProperty(value = "评估类目值")
    private String assessmentVal;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
