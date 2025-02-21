package com.itheima.bolee.security.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_resource")
public class Resource extends BasePojo {

    private String resourceNo;

    private String parentResourceNo;

    private String resourceName;

    private String resourceType;

    private String requestPath;

    private String label;

    private Integer sortNo;

    private String icon;

    private String remark;

    private static final long serialVersionUID = 1L;

    @Builder
    public Resource(Long id, String dataState, String resourceNo, String parentResourceNo, String resourceName, String resourceType, String requestPath, String label, Integer sortNo, String icon, String remark) {
        super(id, dataState);
        this.resourceNo = resourceNo;
        this.parentResourceNo = parentResourceNo;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.requestPath = requestPath;
        this.label = label;
        this.sortNo = sortNo;
        this.icon = icon;
        this.remark = remark;
    }
}
