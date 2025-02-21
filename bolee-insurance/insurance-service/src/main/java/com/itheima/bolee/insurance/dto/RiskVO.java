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
 * @Description：风险表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Risk对象", description="风险表")
public class RiskVO extends BaseVO {

    @Builder
    public RiskVO(Long id,String dataState,String riskTypeKey,String riskTypeName,String riskKey,String riskName,Integer sortNo){
        super(id, dataState);
        this.riskTypeKey=riskTypeKey;
        this.riskTypeName=riskTypeName;
        this.riskKey=riskKey;
        this.riskName=riskName;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险项类型key")
    private String riskTypeKey;

    @ApiModelProperty(value = "风险项类型name")
    private String riskTypeName;

    @ApiModelProperty(value = "风险项key")
    private String riskKey;

    @ApiModelProperty(value = "风险项名称")
    private String riskName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
