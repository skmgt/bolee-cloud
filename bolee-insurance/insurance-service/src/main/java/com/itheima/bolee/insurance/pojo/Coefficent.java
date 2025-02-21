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
 * @Description：系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_coefficent")
@ApiModel(value="Coefficent对象", description="系数项")
public class Coefficent extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Coefficent(String coefficentType,Long id,String dataState,String coefficentKey,String coefficentKeyName,String coefficentVal,Integer sortNo,String remake){
        super(id, dataState);
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName=coefficentKeyName;
        this.coefficentVal=coefficentVal;
        this.sortNo=sortNo;
        this.remake=remake;
        this.coefficentType=coefficentType;
    }

    @ApiModelProperty(value = "系数项key:bznx")
    private String coefficentKey;

    @ApiModelProperty(value = "系数项名称:保障年限")
    private String coefficentKeyName;

    @ApiModelProperty(value = "系数项值")
    private String coefficentVal;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "系数项类型：0、选项系数1、范围类型")
    private String coefficentType;

}
