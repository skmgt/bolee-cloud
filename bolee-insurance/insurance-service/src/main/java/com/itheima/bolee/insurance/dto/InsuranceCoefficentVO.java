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
 * @Description：保险系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceCoefficent对象", description="保险系数项")
public class InsuranceCoefficentVO extends BaseVO {

    @Builder
    public InsuranceCoefficentVO(String isShow,String coefficentAsName,String coefficentType,Long id,String dataState,Long insuranceId,String coefficentKey,String coefficentKeyName,String coefficentValue,BigDecimal score,String isDefault,Integer sortNo,String[] checkedIds,Long[] instanceIds){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName=coefficentKeyName;
        this.coefficentValue=coefficentValue;
        this.coefficentType=coefficentType;
        this.coefficentAsName=coefficentAsName;
        this.score=score;
        this.isShow=isShow;
        this.isDefault=isDefault;
        this.sortNo=sortNo;
        this.checkedIds = checkedIds;
        this.instanceIds = instanceIds;
    }

    @ApiModelProperty(value = "保险产品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "系数项key:bznx")
    private String coefficentKey;

    @ApiModelProperty(value = "系数项名称:保障年限")
    private String coefficentKeyName;

    @ApiModelProperty(value = "系数项类型：0、选项系数1、范围系数 2、年限系数")
    private String coefficentType;

    @ApiModelProperty(value = "系数项别名")
    private String coefficentAsName;

    @ApiModelProperty(value = "系数项值")
    private String coefficentValue;

    @ApiModelProperty(value = "系数项试算值")
    private BigDecimal score;

    @ApiModelProperty(value = "是否默认")
    private String isDefault;

    @ApiModelProperty(value = "是否展示")
    private String isShow;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "批量操作：保险ID")
    private Long[] instanceIds;
}
