package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.insurance.validator.anno.IdentityCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description：客户信息表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CustomerInfo后台对象", description="CustomerInfo后台对象 后台对象不进行数据脱敏")
public class CustomerBackInfoVO extends BaseVO {

    @Builder
    public CustomerBackInfoVO(String identityCard, Long id, String dataState, List<FileVO> fileVOs, String customerId, String remark, String cityName, BigDecimal income, BigDecimal liabilities, String realNameAuthentication, String bindingCard){
        super(id, dataState);
        this.customerId=customerId;
        this.remark=remark;
        this.cityName=cityName;
        this.income=income;
        this.liabilities=liabilities;
        this.realNameAuthentication=realNameAuthentication;
        this.bindingCard=bindingCard;
        this.fileVOs=fileVOs;
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

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "身份证号码")
    @IdentityCard(message = "身份证号格式错误")
    private String identityCard;

}
