package com.itheima.bolee.framework.commons.dto.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * AnalysisCustomerSexDTO
 *
 * @author: wgl
 * @describe: 统计分析客户性别统计
 * @date: 2022/12/28 10:10
 */
@Data
public class AnalysisCustomerSexDTO {

    @ApiModelProperty(value = "保单男性数量")
    private Integer sexManNums;

    @ApiModelProperty(value = "保单女性数量")
    private Integer sexWomanNums;

}