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
 * @Description：省心配流程保障配额记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorryFreeSafeguardQuota对象", description = "省心配流程保障配额记录")
public class WorryFreeSafeguardQuotaVO extends BaseVO {

    @Builder
    public WorryFreeSafeguardQuotaVO(Long id, String dataState, String safeguards, Long medicalAmount, Long accidentAmount, Long dieAmount, Long seriousAmount, Long customerId, Integer sortNo) {
        super(id, dataState);
        this.safeguards = safeguards;
        this.medicalAmount = medicalAmount;
        this.accidentAmount = accidentAmount;
        this.dieAmount = dieAmount;
        this.seriousAmount = seriousAmount;
        this.customerId = customerId;
        this.sortNo = sortNo;
    }

    @ApiModelProperty(value = "保障项列表 json")
    private String safeguards;

    @ApiModelProperty(value = "医疗配额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long medicalAmount;

    @ApiModelProperty(value = "意外配额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accidentAmount;

    @ApiModelProperty(value = "身故配额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dieAmount;

    @ApiModelProperty(value = "重疾配额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seriousAmount;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
