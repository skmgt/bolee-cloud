package com.itheima.bolee.dict.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：数据字典表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_data_dict")
@ApiModel(value="DataDict对象", description="数据字典表")
public class DataDict extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DataDict(Long id, String dataState, String parentKey, String dataKey, String dataValue, String discription) {
        super(id, dataState);
        this.parentKey = parentKey;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.discription = discription;
    }

    @ApiModelProperty(value = "父key")
    private String parentKey;

    @ApiModelProperty(value = "数据字典KEY")
    private String dataKey;

    @ApiModelProperty(value = "值")
    private String dataValue;

    @ApiModelProperty(value = "描述")
    private String discription;


}
