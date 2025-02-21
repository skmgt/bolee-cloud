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
 * @Description：人气保险
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceTop对象", description="人气保险")
public class InsuranceTopVO extends BaseVO {

    @Builder
    public InsuranceTopVO(Long id,String dataState,Long insuranceId,String insuranceName,String categoryNo,String categoryType,String categoryName,Long palnId,BigDecimal price,String priceUnit,Long salesVolume){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.insuranceName=insuranceName;
        this.categoryNo=categoryNo;
        this.categoryType=categoryType;
        this.categoryName=categoryName;
        this.palnId=palnId;
        this.price=price;
        this.priceUnit=priceUnit;
        this.salesVolume=salesVolume;
    }

    @ApiModelProperty(value = "保险ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "保障类型： 1推荐分类  2产品分类 ")
    private String categoryType;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "方案ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long palnId;

    @ApiModelProperty(value = "默认定价")
    private BigDecimal price;

    @ApiModelProperty(value = "默认定价单位：y/d,y/m,y/y")
    private String priceUnit;

    @ApiModelProperty(value = "销量")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long salesVolume;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
