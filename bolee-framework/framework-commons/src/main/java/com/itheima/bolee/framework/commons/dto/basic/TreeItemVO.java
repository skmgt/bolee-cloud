package com.itheima.bolee.framework.commons.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description：树结构体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeItemVO implements Serializable {

    @ApiModelProperty(value = "节点ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public String id;

    @ApiModelProperty(value = "节点父亲ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public String parentId;

    @ApiModelProperty(value = "显示内容")
    public String label;

    @ApiModelProperty(value = "是否选择")
    public Boolean isChecked;

    @ApiModelProperty(value = "显示内容")
    public String systemCode;

    @ApiModelProperty(value = "是否叶子节点")
    public String isLeaf;

    @ApiModelProperty(value = "显示内容")
    public List<TreeItemVO> children;
}
