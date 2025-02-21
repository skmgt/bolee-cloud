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
 * @Description：保障项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SafeguardVO对象", description="保障项")
public class SafeguardVO extends BaseVO {

    @Builder
    public SafeguardVO(Long id,String dataState,String safeguardKey,String safeguardKeyName,String safeguardVal,String safeguardType,Integer sortNo,String remake,String[] checkedIds){
        super(id, dataState);
        this.safeguardKey=safeguardKey;
        this.safeguardKeyName=safeguardKeyName;
        this.safeguardVal=safeguardVal;
        this.safeguardType=safeguardType;
        this.sortNo=sortNo;
        this.remake=remake;
        this.checkedIds = checkedIds;

    }

    @ApiModelProperty(value = "保障项key：mzyl")
    private String safeguardKey;

    @ApiModelProperty(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @ApiModelProperty(value = "保障项值")
    private String safeguardVal;

    @ApiModelProperty(value = "保障项分类：0保障规则 1保障信息")
    private String safeguardType;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
