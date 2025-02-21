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
@TableName("tab_post")
public class Post extends BasePojo {

    private String deptNo;

    private String postNo;

    private String postName;

    private Integer sortNo;

    private String remark;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public Post(Long id, String dataState, String deptNo, String postNo, String postName, Integer sortNo, String remark, String companyNo) {
        super(id, dataState);
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.postName = postName;
        this.sortNo = sortNo;
        this.remark = remark;
        this.companyNo = companyNo;
    }
}
