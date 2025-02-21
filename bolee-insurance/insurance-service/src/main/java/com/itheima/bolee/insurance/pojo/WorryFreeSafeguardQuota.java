package com.itheima.bolee.insurance.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
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
@TableName("tab_worry_free_safeguard_quota")
@ApiModel(value = "WorryFreeSafeguardQuota对象", description = "省心配流程保障配额记录")
public class WorryFreeSafeguardQuota extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WorryFreeSafeguardQuota(Long id, String dataState, String safeguards, Long medicalAmount, Long accidentAmount, Long dieAmount, Long seriousAmount, Long customerId, Integer sortNo) {
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
    @TableField("medicalAmount")
    private Long medicalAmount;

    @ApiModelProperty(value = "意外配额")
    @TableField("accidentAmount")
    private Long accidentAmount;

    @ApiModelProperty(value = "医疗配额")
    @TableField("dieAmount")
    private Long dieAmount;

    @ApiModelProperty(value = "重疾配额")
    @TableField("seriousAmount")
    private Long seriousAmount;

    @ApiModelProperty(value = "用户id")
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
