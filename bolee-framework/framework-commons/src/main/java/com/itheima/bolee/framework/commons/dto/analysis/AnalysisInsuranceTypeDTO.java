package com.itheima.bolee.framework.commons.dto.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * AnalysisCustomerSexDTO
 *
 * @author: wgl
 * @describe: 统计分析投保分类统计分析
 * @date: 2022/12/28 10:10
 */
@Data
public class AnalysisInsuranceTypeDTO {

    @ApiModelProperty(value = "投保分类ID")
    private Integer insuranceTypeId;

    @ApiModelProperty(value = "投保分类名")
    private String insuranceTypeName;

    @ApiModelProperty(value = "对应分类的投保数量")
    private Integer insuranceNums;

}