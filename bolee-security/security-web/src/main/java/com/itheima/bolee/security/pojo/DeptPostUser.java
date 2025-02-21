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
@TableName("tab_dept_post_user")
public class DeptPostUser extends BasePojo {

    private Long userId;

    private String deptNo;

    private String postNo;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public DeptPostUser(Long id, String dataState, Long userId, String deptNo, String postNo,String companyNo) {
        super(id, dataState);
        this.userId = userId;
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.companyNo=companyNo;
    }
}
