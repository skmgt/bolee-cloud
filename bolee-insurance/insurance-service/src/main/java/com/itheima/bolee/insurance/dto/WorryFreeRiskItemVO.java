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
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WorryFreeRiskItem对象", description="")
public class WorryFreeRiskItemVO extends BaseVO {

    @Builder
    public WorryFreeRiskItemVO(Long id,String dataState,String type,String names,Long customerId,Integer sortNo){
        super(id, dataState);
        this.type=type;
        this.names=names;
        this.customerId=customerId;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险类型（意外：accident 医疗：medical 重疾：serious 身故：die 用户标签：person_title）")
    private String type;

    @ApiModelProperty(value = "风险项名称列表 json")
    private String names;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
