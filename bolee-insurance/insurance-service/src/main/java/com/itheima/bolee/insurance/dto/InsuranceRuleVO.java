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
 * @Description：保险规则
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceRule对象", description="保险规则")
public class InsuranceRuleVO extends BaseVO {

    @Builder
    public InsuranceRuleVO(Long id,String dataState,String rulesId,String insuranceId,String rulesType){
        super(id, dataState);
        this.rulesId=rulesId;
        this.insuranceId=insuranceId;
        this.rulesType=rulesType;
    }

    @ApiModelProperty(value = "规则ID")
    private String rulesId;

    @ApiModelProperty(value = "保险ID")
    private String insuranceId;

    @ApiModelProperty(value = "规则类型")
    private String rulesType;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
