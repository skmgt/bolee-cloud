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
 * @Description：保批信息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InsuranceApprove对象", description="保批信息")
public class InsuranceApproveVO extends BaseVO {

    @Builder
    public InsuranceApproveVO(Long id,String dataState,String companyNo,String warrantyNo,String approveState,Date approveTime,Integer sortNo){
        super(id, dataState);
        this.companyNo=companyNo;
        this.warrantyNo=warrantyNo;
        this.approveState=approveState;
        this.approveTime=approveTime;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "公司编号")
    private String companyNo;

    @ApiModelProperty(value = "保险合同编号")
    private String warrantyNo;

    @ApiModelProperty(value = "批保状态(0未批保 1批保发送失败 2批保中 3批保通过 4.保批不通过)")
    private String approveState;

    @ApiModelProperty(value = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;


    @ApiModelProperty(value = "保批详情")
    private List<InsuranceApproveDetailVO> approveDetails;
}
