package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Description：客户关系表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_customer_relation")
@ApiModel(value="CustomerRelation对象", description="客户关系表")
public class CustomerRelation extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CustomerRelation(Long id,String dataState,String relation,String name,String identityCard,String companyNo,Integer sortNo,String socialSecurity,String customerId){
        super(id, dataState);
        this.relation=relation;
        this.name=name;
        this.identityCard=identityCard;
        this.companyNo=companyNo;
        this.sortNo=sortNo;
        this.socialSecurity=socialSecurity;
        this.customerId = customerId;
    }

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "身份证号码")
    private String identityCard;

    @ApiModelProperty(value = "城市编号")
    private String cityNo;

    @ApiModelProperty(value = "收入")
    private BigDecimal income;

    @ApiModelProperty(value = "公司")
    private String companyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "社保状态（0有 1无）")
    private String socialSecurity;

    @ApiModelProperty("系统账号编号")
    private String customerId;

}