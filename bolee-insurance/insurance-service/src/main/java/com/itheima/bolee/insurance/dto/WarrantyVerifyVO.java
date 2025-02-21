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
 * @Description：合同核保
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WarrantyVerify对象", description="合同核保")
public class WarrantyVerifyVO extends BaseVO {

    @Builder
    public WarrantyVerifyVO(Long id,String dataState,String warrantyNo,String companyNo,Integer sortNo,String verifyCode,String verifyMsg){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.companyNo=companyNo;
        this.sortNo=sortNo;
        this.verifyCode=verifyCode;
        this.verifyMsg=verifyMsg;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "核保结果")
    private String verifyCode;

    @ApiModelProperty(value = "核保信息")
    private String verifyMsg;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
