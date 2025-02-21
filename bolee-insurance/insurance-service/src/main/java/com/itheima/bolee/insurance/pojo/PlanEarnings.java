package com.itheima.bolee.insurance.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：方案给付
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_plan_earnings")
@ApiModel(value="PlanEarnings对象", description="方案给付")
public class PlanEarnings extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public PlanEarnings(Long id,String dataState,Long palnId,String earningsType,Integer periods,String earningsJson){
        super(id, dataState);
        this.palnId=palnId;
        this.earningsType=earningsType;
        this.periods=periods;
        this.earningsJson=earningsJson;
    }

    @ApiModelProperty(value = "保险方案id")
    private Long palnId;

    @ApiModelProperty(value = "给付类型:0终身领取 1固定领取")
    private String earningsType;

    @ApiModelProperty(value = "周期数")
    private Integer periods;

    @ApiModelProperty(value = "领取计划")
    private String earningsJson;


}
