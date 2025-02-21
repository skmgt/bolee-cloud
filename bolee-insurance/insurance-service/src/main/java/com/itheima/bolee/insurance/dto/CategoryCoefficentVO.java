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
 * @Description：分类系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CategoryCoefficent对象", description="分类系数项")
public class CategoryCoefficentVO extends BaseVO {

    @Builder
    public CategoryCoefficentVO(String coefficentKeyNamem,Long id,String dataState,String categoryNo,String coefficentKey,Integer sortNo,String remake,String coefficentType){
        super(id, dataState);
        this.categoryNo=categoryNo;
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName = coefficentKeyName;
        this.sortNo=sortNo;
        this.remake=remake;
        this.coefficentType = coefficentType;
    }

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "系数项key")
    private String coefficentKey;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "系数项名称")
    private String coefficentKeyName;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "系数类型")
    private String coefficentType;
}
