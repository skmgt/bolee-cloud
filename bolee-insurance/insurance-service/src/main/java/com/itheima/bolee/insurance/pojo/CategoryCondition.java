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
 * @Description：分类筛选项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_category_condition")
@ApiModel(value="CategoryCondition对象", description="分类筛选项")
public class CategoryCondition extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CategoryCondition(String conditionKeyName,Long id,String dataState,String categoryNo,String conditionKey,Integer sortNo,String remake){
        super(id, dataState);
        this.categoryNo=categoryNo;
        this.conditionKey=conditionKey;
        this.conditionKeyName=conditionKeyName;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "条件项key")
    private String conditionKey;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "条件项名称")
    private String conditionKeyName;
}
