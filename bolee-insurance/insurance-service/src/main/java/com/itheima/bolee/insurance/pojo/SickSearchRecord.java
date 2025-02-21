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
 * @Description：疾病搜索记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_sick_search_record")
@ApiModel(value="SickSearchRecord对象", description="疾病搜索记录")
public class SickSearchRecord extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SickSearchRecord(Long id,String dataState,String content){
        super(id, dataState);
        this.content=content;
    }

    @ApiModelProperty(value = "搜索内容")
    private String content;


}
