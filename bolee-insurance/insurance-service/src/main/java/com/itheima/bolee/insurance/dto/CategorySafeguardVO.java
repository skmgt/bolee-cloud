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
 * @Description：分类保障项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CategorySafeguard对象", description="分类保障项")
public class CategorySafeguardVO extends BaseVO {

    @Builder
    public CategorySafeguardVO(String safeguardKeyName,Long id,String dataState,String categoryNo,String safeguardKey,Integer sortNo,String remake){
        super(id, dataState);
        this.categoryNo=categoryNo;
        this.safeguardKey=safeguardKey;
        this.safeguardKeyName=safeguardKeyName;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "保障项维度")
    private String safeguardKey;

    @ApiModelProperty(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "补充说明")
    private String remake;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

}