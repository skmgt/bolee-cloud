package com.itheima.bolee.framework.commons.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName PayChannelVO.java
 * @Description 支付通道
 */
@Data
@NoArgsConstructor
public class PayChannelVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public PayChannelVO(Long id, String dataState, String channelName, String channelLabel, String domain, String appId, String publicKey, String merchantPrivateKey, String otherConfig, String encryptKey, String remark, List<OtherConfigVO> otherConfigs, String companyNo, String notifyUrl) {
        super(id, dataState);
        this.channelName = channelName;
        this.channelLabel = channelLabel;
        this.domain = domain;
        this.appId = appId;
        this.publicKey = publicKey;
        this.merchantPrivateKey = merchantPrivateKey;
        this.otherConfig = otherConfig;
        this.encryptKey = encryptKey;
        this.remark = remark;
        this.otherConfigs = otherConfigs;
        this.companyNo = companyNo;
        this.notifyUrl = notifyUrl;
    }

    @ApiModelProperty(value = "通道名称")
    private String channelName;

    @ApiModelProperty(value = "通道唯一标记")
    private String channelLabel;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "商户appid")
    private String appId;

    @ApiModelProperty(value = "公钥")
    private String publicKey;

    @ApiModelProperty(value = "商户私钥")
    private String merchantPrivateKey;

    @ApiModelProperty(value = "其他配置")
    private String otherConfig;

    @ApiModelProperty(value = "AES混淆密钥")
    private String encryptKey;

    @ApiModelProperty(value = "说明")
    private String remark;

    @ApiModelProperty(value = "扩展配置",dataType = "OtherConfigVO")
    private List<OtherConfigVO> otherConfigs;


    @ApiModelProperty(value = "商户ID【系统内部识别使用】")
    private String companyNo;

    @ApiModelProperty(value = "回调地址")
    private String notifyUrl;

}
