package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * @Description：绑卡信息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CustomerCard对象", description="绑卡信息")
public class CustomerCardVO extends BaseVO {

    @Builder
    public CustomerCardVO(Long id,String dataState,List<FileVO> fileVOs,String customerId,String bankName,String bankAddress,String bankCardNo,String isDefault){
        super(id, dataState);
        this.customerId=customerId;
        this.bankName=bankName;
        this.bankAddress=bankAddress;
        this.bankCardNo=bankCardNo;
        this.isDefault=isDefault;
        this.fileVOs=fileVOs;
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

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "银行预留手机号")
    private String bankReservedPhoneNum;
}
