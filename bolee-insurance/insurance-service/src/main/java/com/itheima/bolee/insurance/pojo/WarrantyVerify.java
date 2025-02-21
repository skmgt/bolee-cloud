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
 * @Description：合同核保
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_warranty_verify")
@ApiModel(value="WarrantyVerify对象", description="合同核保")
public class WarrantyVerify extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WarrantyVerify(Long id,String dataState,String warrantyNo,String companyNo,Integer sortNo,String verifyCode,String verifyMsg){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.companyNo=companyNo;
        this.sortNo=sortNo;
        this.verifyCode=verifyCode;
        this.verifyMsg=verifyMsg;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "核保结果")
    private String verifyCode;

    @ApiModelProperty(value = "核保信息")
    private String verifyMsg;


}
