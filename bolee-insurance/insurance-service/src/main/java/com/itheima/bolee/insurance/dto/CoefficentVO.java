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
 * @Description：系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Coefficent对象", description="系数项")
public class CoefficentVO extends BaseVO {

    @Builder
    public CoefficentVO(String coefficentType,Long id,String dataState,String coefficentKey,String coefficentKeyName,String coefficentVal,Integer sortNo,String remake){
        super(id, dataState);
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName=coefficentKeyName;
        this.coefficentVal=coefficentVal;
        this.sortNo=sortNo;
        this.remake=remake;
        this.coefficentType=coefficentType;
    }

    @ApiModelProperty(value = "系数项key:bznx")
    private String coefficentKey;

    @ApiModelProperty(value = "系数项名称:保障年限")
    private String coefficentKeyName;

    @ApiModelProperty(value = "系数项值")
    private String coefficentVal;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "系数项类型：0、选项系数1、范围类型")
    private String coefficentType;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
