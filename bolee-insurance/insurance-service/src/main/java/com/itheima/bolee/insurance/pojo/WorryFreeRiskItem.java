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
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_worry_free_risk_item")
@ApiModel(value="WorryFreeRiskItem对象", description="")
public class WorryFreeRiskItem extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WorryFreeRiskItem(Long id,String dataState,String type,String names,Long customerId,Integer sortNo){
        super(id, dataState);
        this.type=type;
        this.names=names;
        this.customerId=customerId;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险类型（意外：accident 医疗：medical 重疾：serious 身故：die 用户标签：person_title）")
    private String type;

    @ApiModelProperty(value = "风险项名称列表 json")
    private String names;

    @ApiModelProperty(value = "用户id")
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
