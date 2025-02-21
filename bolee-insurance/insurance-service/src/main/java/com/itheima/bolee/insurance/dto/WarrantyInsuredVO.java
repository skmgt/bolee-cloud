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
 * @Description：合同被保人
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WarrantyInsured对象", description="合同被保人")
public class WarrantyInsuredVO extends BaseVO {

    @Builder
    public WarrantyInsuredVO(Long id, String dataState, String warrantyNo, String insuredName, String insuredIdentityCard, List<String> warrantyNos){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.insuredName=insuredName;
        this.insuredIdentityCard=insuredIdentityCard;
        this.warrantyNos=warrantyNos;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "被投保人姓名")
    private String insuredName;

    @ApiModelProperty(value = "被投保人身份证号码")
    private String insuredIdentityCard;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "批量操作：合同编号")
    private List<String> warrantyNos;
}
