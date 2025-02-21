package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * StackingIndexVO
 *
 * @author: wgl
 * @describe: 堆叠元素VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "StackingIndexVO对象", description = "统计分析:堆叠元素VO类")
public class StackingIndexVO {

    @Builder
    public StackingIndexVO(List<Long> data, String name){
        this.data=data;
        this.name=name;
    }

    @ApiModelProperty(value = "元素名称")
    private String name;

    @ApiModelProperty(value = "元素值")
    private List<Long> data;
}
