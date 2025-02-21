package com.itheima.bolee.insurance.dto;

import cn.hutool.core.util.StrUtil;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.CustomerInfoUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName WarrantyApplicantVO.java
 * @Description 合同投保人
 */
@Data
@EqualsAndHashCode
@ApiModel(value="WarrantyInsured对象", description="合同投保人")
public class WarrantyApplicantVO {

    @ApiModelProperty(value = "客户基本信息")
    private CustomerBackInfoVO customerInfoVO;

    @ApiModelProperty(value = "绑卡信息")
    private List<CustomerCardVO> customerCardVO;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "年龄")
    private Integer age;



    /**
     * 构造方法私有
     * @param customerBackInfoVO
     * @param customerCardVO
     * @param realName
     */

    private WarrantyApplicantVO(CustomerBackInfoVO customerBackInfoVO, List<CustomerCardVO> customerCardVO, String realName) {
        this.customerInfoVO = customerBackInfoVO;
        this.customerCardVO = customerCardVO;
        this.realName = realName;
        this.sex = CustomerInfoUtil.setSexByIdCard(customerInfoVO.getIdentityCard());
        this.age = CustomerInfoUtil.getAgeByIdCard(customerInfoVO.getIdentityCard());
        this.birthday = CustomerInfoUtil.getBirthDateByIdCard(customerInfoVO.getIdentityCard());
    }

    /**
     * 提供对外提供服务的构造方法
     * @param customerInfoVO
     * @param customerCardVO
     * @param realName
     * @return
     */
    public static WarrantyApplicantVO buildApplicantInfo(CustomerInfoVO customerInfoVO, List<CustomerCardVO> customerCardVO, String realName) {
        CustomerBackInfoVO customerBackInfoVO = BeanConv.toBean(customerInfoVO, CustomerBackInfoVO.class);
        WarrantyApplicantVO warrantyApplicantVO = new WarrantyApplicantVO(customerBackInfoVO,customerCardVO,realName);
        return warrantyApplicantVO;
    }
}
