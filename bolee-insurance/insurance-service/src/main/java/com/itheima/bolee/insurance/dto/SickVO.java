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
 * @Description：疾病表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Sick对象", description="疾病表")
public class SickVO extends BaseVO {

    @Builder
    public SickVO(Long id,String dataState,String sickKey,String sickKeyName,String sickVal,String remake,String sickType,Long createBy){
        super(id, dataState);
        this.sickKey=sickKey;
        this.sickKeyName=sickKeyName;
        this.sickVal=sickVal;
        this.remake=remake;
        this.sickType=sickType;
        super.setCreateBy(createBy);
    }

    @ApiModelProperty(value = "疾病Key")
    private String sickKey;

    @ApiModelProperty(value = "疾病类型")
    private String sickType;

    @ApiModelProperty(value = "咨询问卷Json")
    private String question;

    @ApiModelProperty(value = "疾病名称")
    private String sickKeyName;

    @ApiModelProperty(value = "疾病项值")
    private String sickVal;

    @ApiModelProperty(value = "备注")
    private String remake;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
