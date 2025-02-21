package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FunnelPlotGraphVO
 *
 * @author: wgl
 * @describe: 漏斗图VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="FunnelPlotGraphVO对象", description="统计分析:漏斗图VO类")
public class FunnelPlotChartsVO {

    @Builder
    public FunnelPlotChartsVO(List<String> label, List<FunnelPlotChartsIndexVO> value){
        this.value=value;
        this.label=label;
    }

    @ApiModelProperty(value = "阶段名")
    private List<String> label;

    @ApiModelProperty(value = "每个具体阶段")
    private List<FunnelPlotChartsIndexVO> value;

}