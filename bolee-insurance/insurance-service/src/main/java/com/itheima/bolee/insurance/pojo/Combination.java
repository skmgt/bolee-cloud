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
 * @Description：组合方案
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_combination")
@ApiModel(value="Combination对象", description="组合方案")
public class Combination extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Combination(Long id,String dataState,String combinationName,String riskAnalysis,String riskScenario){
        super(id, dataState);
        this.combinationName=combinationName;
        this.riskAnalysis=riskAnalysis;
        this.riskScenario=riskScenario;
    }

    @ApiModelProperty(value = "组合名称")
    private String combinationName;

    @ApiModelProperty(value = "风险分析")
    private String riskAnalysis;

    @ApiModelProperty(value = "主要分析场景json")
    private String riskScenario;


}
