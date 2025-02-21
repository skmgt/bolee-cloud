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
 * @Description：自选保险
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SelfSelection对象", description="自选保险")
public class SelfSelectionVO extends BaseVO {

    @Builder
    public SelfSelectionVO(Long id,String dataState,Long insuranceId,String relation,Integer sortNo){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.relation=relation;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "保险ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

}
