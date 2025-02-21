package com.itheima.bolee.framework.commons.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description：菜单meta属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuMetaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "角色")
    private List<String> roles;
}
