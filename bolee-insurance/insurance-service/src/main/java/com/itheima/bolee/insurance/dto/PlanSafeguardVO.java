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
 * @Description：方案保障项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PlanSafeguard对象", description="方案保障项")
public class PlanSafeguardVO extends BaseVO {

    @Builder
    public PlanSafeguardVO(Long id,String dataState,Long planId,String safeguardKey,String safeguardKeyName,String safeguardValue,String safeguardAsName,String safeguardType,String position,String remake,Integer sortNo){
        super(id, dataState);
        this.planId=planId;
        this.safeguardKey=safeguardKey;
        this.safeguardKeyName=safeguardKeyName;
        this.safeguardValue=safeguardValue;
        this.safeguardAsName=safeguardAsName;
        this.safeguardType=safeguardType;
        this.position=position;
        this.remake=remake;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "保险方案ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long planId;

    @ApiModelProperty(value = "保障项key：mzyl")
    private String safeguardKey;

    @ApiModelProperty(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @ApiModelProperty(value = "保障项值")
    private String safeguardValue;

    @ApiModelProperty(value = "表单：全量保障项值")
    private List<SafeguardVO> safeguardVOs;

    @ApiModelProperty(value = "保障项别名：一般门诊费用")
    private String safeguardAsName;

    @ApiModelProperty(value = "保障类型（0保障规则 1 保障信息）")
    private String safeguardType;

    @ApiModelProperty(value = "显示位置：0 列表页 1 详情页")
    private String position;

    @ApiModelProperty(value = "保障内容补充说明")
    private String remake;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
