package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * SelfSelectionIndexVO
 *
 * @author: wgl
 * @describe: 自选保险首页列表
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SelfSelection对象", description = "自选保险")
public class SelfSelectionIndexVO extends BaseVO {

    @Builder
    public SelfSelectionIndexVO(Long id, String dataState, SelfSelectionVO selections, InsurancePlanVO insurancePlanVO) {
        super(id, dataState);
        this.selections = selections;
        this.insurancePlanVO = insurancePlanVO;
    }

    @ApiModelProperty(value = "自选保险列表")
    private SelfSelectionVO selections;


    @ApiModelProperty(value = "最便宜保险计划")
    private InsurancePlanVO insurancePlanVO;
}
