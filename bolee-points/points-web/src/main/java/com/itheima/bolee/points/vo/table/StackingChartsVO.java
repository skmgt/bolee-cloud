package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * StackingChartsVO
 *
 * @author: wgl
 * @describe: StackingChartsVO对象--堆叠图VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "StackingChartsVO对象", description = "统计分析:堆叠图VO类")
public class StackingChartsVO {

    @Builder
    public StackingChartsVO(List<String> label, List<StackingIndexVO> value){
        this.value=value;
        this.label=label;
    }

    @ApiModelProperty(value = "x轴")
    private List<String> label;

    @ApiModelProperty(value = "y轴数据")
    private List<StackingIndexVO> value;
}
