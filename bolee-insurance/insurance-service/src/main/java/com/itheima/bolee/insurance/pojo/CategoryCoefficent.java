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
 * @Description：分类系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_category_coefficent")
@ApiModel(value="CategoryCoefficent对象", description="分类系数项")
public class CategoryCoefficent extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CategoryCoefficent(String coefficentKeyNamem,Long id,String dataState,String categoryNo,String coefficentKey,Integer sortNo,String remake){
        super(id, dataState);
        this.categoryNo=categoryNo;
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName = coefficentKeyName;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "系数项key")
    private String coefficentKey;

    @ApiModelProperty(value = "系数项名称:保障年限")
    private String coefficentKeyName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

}
