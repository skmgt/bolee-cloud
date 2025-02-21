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
 * @Description：地方表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_places")
@ApiModel(value="Places对象", description="地方表")
public class Places extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Places(Long id, String dataState, Long parentId, String cityName) {
        super(id, dataState);
        this.parentId = parentId;
        this.cityName = cityName;
    }

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "名称")
    private String cityName;


}
