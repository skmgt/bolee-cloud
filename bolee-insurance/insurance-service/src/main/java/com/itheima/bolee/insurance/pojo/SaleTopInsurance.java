package com.itheima.bolee.insurance.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
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
@TableName("tab_sale_top_insurance")
@ApiModel(value = "SaleTopInsurance对象", description = "")
public class SaleTopInsurance extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SaleTopInsurance(Long id, String dataState, String checkRule, Long insuranceId, String insuranceName, String insuranceDetail, String comment, String remake, Long categoryNo, String companyNo) {
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
    private Long categoryNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;


}
