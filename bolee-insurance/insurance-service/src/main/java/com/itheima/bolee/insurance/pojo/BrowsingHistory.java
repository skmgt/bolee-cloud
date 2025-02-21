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
 * @Description：浏览记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_browsing_history")
@ApiModel(value="BrowsingHistory对象", description="浏览记录")
public class BrowsingHistory extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public BrowsingHistory(Long id,String dataState,Long insuranceId,Integer sortNo){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "保险ID")
    private Long insuranceId;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
