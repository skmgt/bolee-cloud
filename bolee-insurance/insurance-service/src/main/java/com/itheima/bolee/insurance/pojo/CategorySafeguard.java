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
 * @Description：分类保障项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_category_safeguard")
@ApiModel(value="CategorySafeguard对象", description="分类保障项")
public class CategorySafeguard extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CategorySafeguard(String safeguardKeyName,Long id,String dataState,String categoryNo,String safeguardKey,Integer sortNo,String remake){
        super(id, dataState);
        this.categoryNo=categoryNo;
        this.safeguardKey=safeguardKey;
        this.safeguardKeyName=safeguardKeyName;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "条例维度")
    private String safeguardKey;

    @ApiModelProperty(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "补充说明")
    private String remake;

}
