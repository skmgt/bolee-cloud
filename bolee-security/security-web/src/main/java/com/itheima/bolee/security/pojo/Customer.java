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
@TableName("tab_customer")
public class Customer extends BasePojo {

    private String username;

    private String openId;

    private String password;

    private String nickName;

    private String email;

    private String realName;

    private String mobile;

    private String sex;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public Customer(Long id, String dataState, String username, String openId, String password,
                    String nickName, String email, String realName, String mobile, String sex) {
        super(id, dataState);
        this.username = username;
        this.openId = openId;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.realName = realName;
        this.mobile = mobile;
        this.sex = sex;
        this.companyNo=companyNo;
    }
}
