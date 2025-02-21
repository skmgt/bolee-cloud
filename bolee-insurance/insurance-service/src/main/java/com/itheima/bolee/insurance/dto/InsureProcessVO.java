package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName InsureProcessVO.java
 * @Description 投保、试算处理对象
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value="InsureProcessVO对象", description="投保、试算处理对象")
public class InsureProcessVO implements Serializable {

    @ApiModelProperty(value = "保险信息")
    private InsuranceVO insuranceVO;

    @ApiModelProperty(value = "保险方案")
    private InsurancePlanVO insurancePlanVO;

    @ApiModelProperty(value = "公司信息")
    private CompanyVO companyVO;

    @ApiModelProperty(value = "保障项信息）")
    private List<PlanSafeguardVO> safeguardVOs;

    @ApiModelProperty(value = "保险系数项")
    private List<InsuranceCoefficentVO> coefficents;

    @Builder
    public InsureProcessVO(InsuranceVO insuranceVO, InsurancePlanVO insurancePlanVO, CompanyVO companyVO, List<PlanSafeguardVO> safeguardVOs, List<InsuranceCoefficentVO> coefficents) {
        this.insuranceVO = insuranceVO;
        this.insurancePlanVO = insurancePlanVO;
        this.companyVO = companyVO;
        this.safeguardVOs = safeguardVOs;
        this.coefficents = coefficents;
    }
}
