package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description：省心配流程节点记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WorryFreeFlowNode对象", description="省心配流程节点记录")
public class WorryFreeFlowNodeVO extends BaseVO {

    @Builder
    public WorryFreeFlowNodeVO(Long id,String dataState,String nodes,Long customerId,Integer sortNo){
        super(id, dataState);
        this.nodes=nodes;
        this.customerId=customerId;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险项名称列表 json")
    private String nodes;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
