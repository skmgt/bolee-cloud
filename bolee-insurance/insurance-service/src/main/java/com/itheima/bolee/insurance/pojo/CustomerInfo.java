package com.itheima.bolee.insurance.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description：客户信息表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_customer_info")
@ApiModel(value="CustomerInfo对象", description="客户信息表")
public class CustomerInfo extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CustomerInfo(String identityCard,Long id,String dataState,String customerId,String remark,String cityName,BigDecimal income,BigDecimal liabilities,String realNameAuthentication,String bindingCard){
        super(id, dataState);
        this.customerId=customerId;
        this.remark=remark;
        this.cityName=cityName;
        this.income=income;
        this.liabilities=liabilities;
        this.realNameAuthentication=realNameAuthentication;
        this.bindingCard=bindingCard;
        this.identityCard = identityCard;
    }

    @ApiModelProperty(value = "系统账号编号")
    private String customerId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "常驻城市")
    private String cityName;

    @ApiModelProperty(value = "收入")
    private BigDecimal income;

    @ApiModelProperty(value = "负债")
    private BigDecimal liabilities;

    @ApiModelProperty(value = "实名认证（0是 1否）")
    private String realNameAuthentication;

    @ApiModelProperty(value = "绑卡（0是 1否）")
    private String bindingCard;

    @ApiModelProperty(value = "身份证号码")
    private String identityCard;
}
