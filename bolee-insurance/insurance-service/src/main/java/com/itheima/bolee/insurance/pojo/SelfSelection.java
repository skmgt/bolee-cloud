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
 * @Description：自选保险
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_self_selection")
@ApiModel(value="SelfSelection对象", description="自选保险")
public class SelfSelection extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SelfSelection(Long id,String dataState,Long insuranceId,String relation,Integer sortNo){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.relation=relation;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "保险ID")
    private Long insuranceId;

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
