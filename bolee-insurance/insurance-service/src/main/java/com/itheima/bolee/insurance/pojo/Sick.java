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
 * @Description：疾病表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_sick")
@ApiModel(value="Sick对象", description="疾病表")
public class Sick extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Sick(Long id,String dataState,String sickKey,String sickKeyName,String sickVal,String remake,String sickType){
        super(id, dataState);
        this.sickKey=sickKey;
        this.sickKeyName=sickKeyName;
        this.sickVal=sickVal;
        this.remake=remake;
        this.sickType=sickType;
    }

    @ApiModelProperty(value = "疾病Key")
    private String sickKey;

    @ApiModelProperty(value = "疾病类型")
    private String sickType;

    @ApiModelProperty(value = "咨询问卷Json")
    private String question;

    @ApiModelProperty(value = "疾病名称")
    private String sickKeyName;

    @ApiModelProperty(value = "疾病项值")
    private String sickVal;

    @ApiModelProperty(value = "备注")
    private String remake;


}
