package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LineGraphVO
 *
 * @author: wgl
 * @describe: 直线图,柱状图VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="LineGraphVO对象", description="统计分析:直线图,柱状图VO类")
public class LineChartsVO {

    @Builder
    public LineChartsVO(List label, List value){
        this.value=value;
        this.label=label;
    }
    @ApiModelProperty(value = "x轴横坐标")
    private List label;

    @ApiModelProperty(value = "y轴纵坐标")
    private List value;
}
