package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：绑卡信息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_customer_card")
@ApiModel(value="CustomerCard对象", description="绑卡信息")
public class CustomerCard extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CustomerCard(Long id,String dataState,String customerId,String bankName,String bankAddress,String bankCardNo,String isDefault,String bankReservedPhoneNum){
        super(id, dataState);
        this.customerId=customerId;
        this.bankName=bankName;
        this.bankAddress=bankAddress;
        this.bankCardNo=bankCardNo;
        this.isDefault=isDefault;
        this.bankReservedPhoneNum = bankReservedPhoneNum;
    }

    @ApiModelProperty(value = "系统账号编号")
    private String customerId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行地址")
    private String bankAddress;

    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "是否默认（0是 1否）")
    private String isDefault;

    @ApiModelProperty(value = "银行预留手机号")
    private String bankReservedPhoneNum;

}
