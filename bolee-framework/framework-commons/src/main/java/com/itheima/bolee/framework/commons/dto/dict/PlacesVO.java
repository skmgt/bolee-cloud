package com.itheima.bolee.framework.commons.dto.dict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：地方表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlacesVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public PlacesVO(Long id, String dataState, Long parentId, String cityName) {
        super(id, dataState);
        this.parentId = parentId;
        this.cityName = cityName;
    }

    @ApiModelProperty(value = "父ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    @ApiModelProperty(value = "名称")
    private String cityName;


}
