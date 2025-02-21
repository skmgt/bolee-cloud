package com.itheima.bolee.framework.commons.dto.dict;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
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
public class DataDictVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public DataDictVO(Long id, String dataState, String parentKey,
                       String dataKey, String dataValue, String discription) {
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
