package com.itheima.bolee.trade.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：交易渠道表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_pay_channel")
@ApiModel(value="PayChannel对象", description="交易渠道表")
public class PayChannel extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public PayChannel(Long id, String dataState, String channelName, String channelLabel, String domain, String appId, String publicKey, String merchantPrivateKey, String otherConfig, String encryptKey, String remark, String companyNo, String notifyUrl) {
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

    @ApiModelProperty(value = "支付公钥")
    private String publicKey;

    @ApiModelProperty(value = "商户私钥")
    private String merchantPrivateKey;

    @ApiModelProperty(value = "其他配置")
    private String otherConfig;

    @ApiModelProperty(value = "AES混淆密钥")
    private String encryptKey;

    @ApiModelProperty(value = "说明")
    private String remark;

    @ApiModelProperty(value = "商户ID【系统内部识别使用】")
    private String companyNo;

    @ApiModelProperty(value = "回调地址")
    private String notifyUrl;

}
