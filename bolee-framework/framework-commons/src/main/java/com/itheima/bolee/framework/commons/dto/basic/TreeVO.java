package com.itheima.bolee.framework.commons.dto.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 树显示类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeVO implements Serializable {

	@ApiModelProperty(value = "tree数据")
	private List<TreeItemVO> items;

	@ApiModelProperty(value = "选择节点")
	private List<String> checkedIds;

	@ApiModelProperty(value = "展开项")
	private List<String> expandedIds;

}
