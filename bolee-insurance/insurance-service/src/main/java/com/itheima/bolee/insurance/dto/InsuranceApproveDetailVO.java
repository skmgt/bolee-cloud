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
 * @Description：保批信息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceApproveDetail对象", description="保批信息")
public class InsuranceApproveDetailVO extends BaseVO {

    @Builder
    public InsuranceApproveDetailVO(Long id,String dataState,Long approveId,String type,String changeFieldKey,String title,String label,String modifyCont,String modifyReason,String approveTime,Integer sortNo){
        super(id, dataState);
        this.approveId=approveId;
        this.type=type;
        this.changeFieldKey=changeFieldKey;
        this.title=title;
        this.label=label;
        this.modifyCont=modifyCont;
        this.modifyReason=modifyReason;
        this.approveTime=approveTime;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "报批id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long approveId;

    @ApiModelProperty(value = "投保人：0  被保人：1")
    private String type;

    @ApiModelProperty(value = "修改字段键")
    private String changeFieldKey;

    @ApiModelProperty(value = "修改字段名")
    private String title;

    @ApiModelProperty(value = "修改前的内容")
    private String label;

    @ApiModelProperty(value = "修改后的内容")
    private String modifyCont;

    @ApiModelProperty(value = "原因")
    private String modifyReason;

    @ApiModelProperty(value = "修改时间")
    private String approveTime;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
