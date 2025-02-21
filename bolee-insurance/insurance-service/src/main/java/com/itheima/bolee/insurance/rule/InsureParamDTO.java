package com.itheima.bolee.insurance.rule;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * InsureParamDTO
 *
 * @author: wgl
 * @describe: 投保参数对象
 * @date: 2022/12/28 10:10
 */
@Data
public class InsureParamDTO {

    @Label("申请人信息")
    private ApplicantInfo applicantInfo;

    @Label("保险基本信息")
    private InsuranceDTO insuranceDTO;

    @Label("保险方案列表")
    private List<InsurePlanDTO> planDTOList;

    @Label("保险方案关联的保障项列表")
    private Map<String,InsurePlanDTO> safeItemDTO;
}
