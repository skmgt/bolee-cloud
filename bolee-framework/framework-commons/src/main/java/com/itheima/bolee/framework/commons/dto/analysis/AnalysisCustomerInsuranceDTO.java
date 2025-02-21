package com.itheima.bolee.framework.commons.dto.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AnalysisCustomerInsuranceDTO
 *
 * @author: wgl
 * @describe: 统计每日客户保险数量数据传输对象
 * @date: 2022/12/28 10:10
 */
@Data
public class AnalysisCustomerInsuranceDTO {

    @ApiModelProperty(value = "当日总保费")
    private Long money;

    @ApiModelProperty(value = "当日保单数")
    private Long insuranceNums;

}
