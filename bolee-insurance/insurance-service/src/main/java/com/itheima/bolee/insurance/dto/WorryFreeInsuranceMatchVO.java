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
 * @Description：省心配保险推荐记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WorryFreeInsuranceMatch对象", description="省心配保险推荐记录")
public class WorryFreeInsuranceMatchVO extends BaseVO {

    @Builder
    public WorryFreeInsuranceMatchVO(Long id,String dataState,String type,String insuranceJson,String insurancePlan,Long amount,String solution,String planSuggest,Long customerId,Integer sortNo,String insuranceId,BigDecimal price){
        super(id, dataState);
        this.type=type;
        this.insuranceJson=insuranceJson;
        this.insurancePlan=insurancePlan;
        this.amount=amount;
        this.solution=solution;
        this.planSuggest=planSuggest;
        this.customerId=customerId;
        this.sortNo=sortNo;
        this.insuranceId = insuranceId;
        this.price = price;
    }

    @ApiModelProperty(value = "风险类型（意外：accident 医疗：medical 重疾：serious 身故：die 用户标签：person_title）")
    private String type;

    @ApiModelProperty(value = "保险json")
    private String insuranceJson;

    @ApiModelProperty(value = "保险id")
    private String insuranceId;
    @ApiModelProperty(value = "保险方案")
    private String insurancePlan;

    @ApiModelProperty(value = "分配的保额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long amount;

    @ApiModelProperty(value = "解决方案")
    private String solution;

    @ApiModelProperty(value = "方案建议")
    private String planSuggest;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "保费")
    private BigDecimal price;
}
