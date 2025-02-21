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
import java.time.LocalDateTime;
import java.util.Date;
/**
 * @Description：合同续期
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WarrantyRenewal对象", description="合同续期")
public class WarrantyRenewalVO extends BaseVO {

    @Builder
    public WarrantyRenewalVO(Long id,String dataState,String warrantyNo,Integer sortNo,LocalDateTime safeguardStartTime,LocalDateTime safeguardEndTime,String companyNo){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.sortNo=sortNo;
        this.safeguardStartTime=safeguardStartTime;
        this.safeguardEndTime=safeguardEndTime;
        this.companyNo=companyNo;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "保障起始时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime safeguardStartTime;

    @ApiModelProperty(value = "保障截止时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime safeguardEndTime;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
