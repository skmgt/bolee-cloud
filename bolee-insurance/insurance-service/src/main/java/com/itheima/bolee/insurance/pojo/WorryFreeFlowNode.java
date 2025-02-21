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
 * @Description：省心配流程节点记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_worry_free_flow_node")
@ApiModel(value="WorryFreeFlowNode对象", description="省心配流程节点记录")
public class WorryFreeFlowNode extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WorryFreeFlowNode(Long id,String dataState,String nodes,Long customerId,Integer sortNo){
        super(id, dataState);
        this.nodes=nodes;
        this.customerId=customerId;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险项名称列表 json")
    private String nodes;

    @ApiModelProperty(value = "用户id")
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
