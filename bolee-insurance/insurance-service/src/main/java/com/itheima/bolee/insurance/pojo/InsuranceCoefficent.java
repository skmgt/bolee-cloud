package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：保险系数项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_coefficent")
@ApiModel(value="InsuranceCoefficent对象", description="保险系数项")
public class InsuranceCoefficent extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceCoefficent(String isShow,String coefficentType, Long id, String dataState, Long insuranceId, String coefficentKey, String coefficentKeyName, String coefficentValue, BigDecimal score, String isDefault, Integer sortNo){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.coefficentKey=coefficentKey;
        this.coefficentKeyName=coefficentKeyName;
        this.coefficentValue=coefficentValue;
        this.coefficentType=coefficentType;
        this.score=score;
        this.isDefault=isDefault;
        this.isShow=isShow;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "保险产品id")
    private Long insuranceId;

    @ApiModelProperty(value = "系数项key:bznx")
    private String coefficentKey;

    @ApiModelProperty(value = "系数项名称:保障年限")
    private String coefficentKeyName;

    @ApiModelProperty(value = "系数项value")
    private String coefficentValue;

    @ApiModelProperty(value = "系数项类型：0、选项系数1、范围系数 2、年限系数")
    private String coefficentType;

    @ApiModelProperty(value = "系数项别名")
    private String coefficentAsName;

    @ApiModelProperty(value = "系数项试算值")
    private BigDecimal score;

    @ApiModelProperty(value = "是否默认")
    private String isDefault;

    @ApiModelProperty(value = "是否展示")
    private String isShow;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
