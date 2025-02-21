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
 * @Description：组合保险
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_combination_insurance")
@ApiModel(value="CombinationInsurance对象", description="组合保险")
public class CombinationInsurance extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CombinationInsurance(Long id,String dataState,Long combinationId,Long insuranceId,String suggest,String thinking,String priority){
        super(id, dataState);
        this.combinationId=combinationId;
        this.insuranceId=insuranceId;
        this.suggest=suggest;
        this.thinking=thinking;
        this.priority=priority;
    }

    @ApiModelProperty(value = "保险组合ID")
    private Long combinationId;

    @ApiModelProperty(value = "保险ID")
    private Long insuranceId;

    @ApiModelProperty(value = "建议")
    private String suggest;

    @ApiModelProperty(value = "配置思路")
    private String thinking;

    @ApiModelProperty(value = "优先级")
    private String priority;


}
