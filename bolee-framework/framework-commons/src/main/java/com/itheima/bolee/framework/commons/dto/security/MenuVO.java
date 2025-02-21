package com.itheima.bolee.framework.commons.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 动态菜单VO对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuVO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
	@ApiModelProperty(value = "路由名字")
	private  String name;

	@ApiModelProperty(value = "请求路径")
	private String path;

	@ApiModelProperty(value = "高亮子菜单")
	private String redirect;

	@ApiModelProperty(value = "模块跳转目标")
	private String component;

	// 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
	@ApiModelProperty(value = "是否显示")
	private Boolean hidden;

	@ApiModelProperty(value = "子菜单")
	private List<MenuVO> children;

	@ApiModelProperty(value = "meta属性")
	private MenuMetaVO meta;
}
