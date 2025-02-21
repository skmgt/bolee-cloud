package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：合同续期
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_warranty_renewal")
@ApiModel(value="WarrantyRenewal对象", description="合同续期")
public class WarrantyRenewal extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public WarrantyRenewal(Long id,String dataState,String warrantyNo,Integer sortNo,LocalDateTime safeguardStartTime,LocalDateTime safeguardEndTime,String companyNo){
        super(id, dataState);
        this.warrantyNo=warrantyNo;
        this.sortNo=sortNo;
        this.safeguardStartTime=safeguardStartTime;
        this.safeguardEndTime=safeguardEndTime;
        this.companyNo=companyNo;
    }

    @ApiModelProperty(value = "保单编号")
    private String warrantyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "保障起始时间")
    private LocalDateTime safeguardStartTime;

    @ApiModelProperty(value = "保障截止时间")
    private LocalDateTime safeguardEndTime;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;


}
