package com.itheima.bolee.framework.commons.dto.report;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description：保险分类
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CategoryReportVO对象", description="保险分类报表对象")
public class CategoryReportVO extends BaseVO {


    @Builder
    public CategoryReportVO(Long id, String dataState, String parentCategoryNo, String categoryNo, String categoryName, String icon, String leafNode, String showIndex, String categoryType, Integer sortNo, String remake, String checkRule, String[] checkedIds, String[] checkedCategoryNos, List<Long> nodeFloors) {
        super(id, dataState);
        this.parentCategoryNo = parentCategoryNo;
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
        this.icon = icon;
        this.leafNode = leafNode;
        this.showIndex = showIndex;
        this.categoryType = categoryType;
        this.sortNo = sortNo;
        this.remake = remake;
        this.checkRule = checkRule;
        this.checkedIds = checkedIds;
        this.checkedCategoryNos = checkedCategoryNos;
        this.nodeFloors = nodeFloors;
    }

    @ApiModelProperty(value = "父分类编号")
    private String parentCategoryNo;

    @ApiModelProperty(value = "分类编号")
    private String categoryNo;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否叶子节点（0是 否1）")
    private String leafNode;

    @ApiModelProperty(value = "是否显示在首页（0是 否1）")
    private String showIndex;

    @ApiModelProperty(value = "分类类型：0推荐分类  1产品分类 ")
    private String categoryType;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "分类补充说明")
    private String remake;

    @ApiModelProperty(value = "校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿")
    private String checkRule;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "TREE结构：选中分类编号")
    private String[] checkedCategoryNos;

    @ApiModelProperty(value = "节点层级:最多5层")
    private List<Long> nodeFloors;
}
