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
 * @Description：方案给付
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PlanEarnings对象", description="方案给付")
public class PlanEarningsVO extends BaseVO {

    @Builder
    public PlanEarningsVO(Long id,String dataState,Long palnId,String earningsType,Integer periods,String earningsJson){
        super(id, dataState);
        this.palnId=palnId;
        this.earningsType=earningsType;
        this.periods=periods;
        this.earningsJson=earningsJson;
    }

    @ApiModelProperty(value = "保险方案id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long palnId;

    @ApiModelProperty(value = "给付类型:0终身领取 1固定领取")
    private String earningsType;

    @ApiModelProperty(value = "周期数")
    private Integer periods;

    @ApiModelProperty(value = "领取计划")
    private String earningsJson;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
