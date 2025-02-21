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
 * @Description：初筛结果
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_sieving")
@ApiModel(value="InsuranceSieving对象", description="初筛结果")
public class InsuranceSieving extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceSieving(Long id,String dataState,String name,String sicksName,String sicksKey,Boolean result,Integer sortNo){
        super(id, dataState);
        this.name=name;
        this.sicksName=sicksName;
        this.sicksKey=sicksKey;
        this.result=result;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "疾病列表")
    private String sicksName;

    @ApiModelProperty(value = "疾病key列表")
    private String sicksKey;

    @ApiModelProperty(value = "初筛结果")
    private Boolean result;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
