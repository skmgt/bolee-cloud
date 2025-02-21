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
 * @Description：组合保险
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CombinationInsurance对象", description="组合保险")
public class CombinationInsuranceVO extends BaseVO {

    @Builder
    public CombinationInsuranceVO(Long id,String dataState,Long combinationId,Long insuranceId,String suggest,String thinking,String priority){
        super(id, dataState);
        this.combinationId=combinationId;
        this.insuranceId=insuranceId;
        this.suggest=suggest;
        this.thinking=thinking;
        this.priority=priority;
    }

    @ApiModelProperty(value = "保险组合ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long combinationId;

    @ApiModelProperty(value = "保险ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "建议")
    private String suggest;

    @ApiModelProperty(value = "配置思路")
    private String thinking;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "组成组合的保险列表")
    private List<InsuranceVO> insurances;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
