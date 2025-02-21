package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * CombinationDetailVO
 *
 * @author: wgl
 * @describe: 保险方案详情对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CombinationDetailVO对象", description="保险方案详情对象")
public class CombinationDetailVO extends BaseVO {

    @ApiModelProperty(value = "保险方案")
    private CombinationVO combination;

    @ApiModelProperty(value = "关联的保险列表")
    private List<InsuranceVO> insuranceList;

    @ApiModelProperty(value = "最低缴保险计划,Key为保险id")
    private Map<String,InsurancePlanVO> plans;

    @ApiModelProperty(value = "展示的保障项，key为保险id")
    private Map<String,List<PlanSafeguardVO>> safes;

    @ApiModelProperty(value = "方案金额")
    private String totalMoney;
}
