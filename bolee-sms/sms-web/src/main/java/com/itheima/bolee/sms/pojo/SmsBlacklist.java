package com.itheima.bolee.sms.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：黑名单表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_sms_blacklist")
@ApiModel(value="SmsBlacklist对象", description="黑名单表")
public class SmsBlacklist extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SmsBlacklist(Long id, String dataState, String mobile,String companyNo) {
        super(id, dataState);
        this.mobile = mobile;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;


}
