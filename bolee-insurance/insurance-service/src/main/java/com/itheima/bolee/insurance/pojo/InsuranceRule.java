package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：保险规则
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_rule")
@ApiModel(value="InsuranceRule对象", description="保险规则")
public class InsuranceRule extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceRule(Long id,String dataState,String rulesId,String insuranceId,String rulesType){
        super(id, dataState);
        this.rulesId=rulesId;
        this.insuranceId=insuranceId;
        this.rulesType=rulesType;
    }

    @ApiModelProperty(value = "规则ID")
    private String rulesId;

    @ApiModelProperty(value = "保险ID")
    private String insuranceId;

    @ApiModelProperty(value = "规则类型")
    private String rulesType;


}
