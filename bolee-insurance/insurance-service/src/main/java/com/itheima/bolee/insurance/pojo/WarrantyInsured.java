package com.itheima.bolee.insurance.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.itheima.bolee.framework.commons.utils.CustomerInfoUtil;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.insurance.dto.CustomerCardVO;
import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description：合同被保人
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_warranty_insured")
@ApiModel(value="WarrantyInsured对象", description="合同被保人")
public class WarrantyInsured extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WarrantyInsured(String companyNo,Long id,String dataState,String warrantyNo,String insuredName,String insuredIdentityCard){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.insuredName=insuredName;
        this.insuredIdentityCard=insuredIdentityCard;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "被投保人姓名")
    private String insuredName;

    @ApiModelProperty(value = "被投保人身份证号码")
    private String insuredIdentityCard;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "年龄")
    @TableField(exist = false)
    private Integer age;

    @ApiModelProperty(value = "出生日期")
    @TableField(exist = false)
    private String birthday;

    @ApiModelProperty(value = "性别")
    @TableField(exist = false)
    private String sex;

    public WarrantyInsured(String warrantyNo, String insuredName, String insuredIdentityCard, String companyNo) {
        this.warrantyNo = warrantyNo;
        this.insuredName = insuredName;
        this.insuredIdentityCard = insuredIdentityCard;
        this.companyNo = companyNo;
        this.sex = CustomerInfoUtil.setSexByIdCard(insuredIdentityCard);
        this.age = CustomerInfoUtil.getAgeByIdCard(insuredIdentityCard);
        this.birthday = CustomerInfoUtil.getBirthDateByIdCard(insuredIdentityCard);
    }

}
