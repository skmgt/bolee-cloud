package com.itheima.bolee.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SaleTopInsurance对象", description = "")
public class SaleTopInsuranceVO extends BaseVO {

    @Builder
    public SaleTopInsuranceVO(Long id, String dataState, String checkRule, Long insuranceId, String insuranceName, String insuranceDetail, String comment, String remake, Long categoryNo, String companyNo) {
        super(id, dataState);
        this.checkRule = checkRule;
        this.insuranceId = insuranceId;
        this.insuranceName = insuranceName;
        this.insuranceDetail = insuranceDetail;
        this.comment = comment;
        this.remake = remake;
        this.categoryNo = categoryNo;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "保险类型")
    private String checkRule;

    @ApiModelProperty(value = "保险id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "保险详情")
    private String insuranceDetail;

    @ApiModelProperty(value = "产品点评")
    private String comment;

    @ApiModelProperty(value = "产品描述")
    private String remake;

    @ApiModelProperty(value = "分类编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
