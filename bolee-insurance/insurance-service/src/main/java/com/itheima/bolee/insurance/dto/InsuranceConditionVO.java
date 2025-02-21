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

/**
 * @Description：保险筛选项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceCondition对象", description="保险筛选项")
public class InsuranceConditionVO extends BaseVO {

    @Builder
    public InsuranceConditionVO(Long id, String dataState, String insuranceId, String conditionKey, String conditionKeyName, String conditionVal, Integer sortNo, String remake, String[] checkedIds,Long[] insuranceIds){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.conditionKey=conditionKey;
        this.conditionKeyName=conditionKeyName;
        this.conditionVal=conditionVal;
        this.sortNo=sortNo;
        this.remake=remake;
        this.checkedIds = checkedIds;
        this.insuranceIds= insuranceIds;
    }

    @ApiModelProperty(value = "保险产品ID")
    private String insuranceId;

    @ApiModelProperty(value = "条件项key")
    private String conditionKey;

    @ApiModelProperty(value = "条件项名称")
    private String conditionKeyName;

    @ApiModelProperty(value = "条件项值")
    private String conditionVal;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "批量操作：保险id")
    private Long[] insuranceIds;
}
