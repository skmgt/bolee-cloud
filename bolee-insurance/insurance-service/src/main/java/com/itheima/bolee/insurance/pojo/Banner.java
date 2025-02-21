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
@TableName("tab_banner")
@ApiModel(value="Banner对象", description="")
public class Banner extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Banner(String bannerType,Long id,String dataState,Integer sortNo,String url){
        super(id, dataState);
        this.sortNo=sortNo;
        this.url=url;
        this.bannerType=bannerType;
    }

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "调整路径")
    private String url;

    @ApiModelProperty(value = "导航类型（0首页 1推荐）")
    private String bannerType;
}
