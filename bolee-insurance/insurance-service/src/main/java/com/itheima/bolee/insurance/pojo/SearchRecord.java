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
 * @Description：搜索记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_search_record")
@ApiModel(value="SearchRecord对象", description="搜索记录")
public class SearchRecord extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SearchRecord(Long id,String dataState,String content,Long createBy){
        super(id, dataState);
        super.setCreateBy(createBy);
        this.content=content;
    }

    @ApiModelProperty(value = "搜索内容")
    private String content;


}
