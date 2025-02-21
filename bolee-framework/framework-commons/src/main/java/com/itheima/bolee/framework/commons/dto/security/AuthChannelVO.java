package com.itheima.bolee.framework.commons.dto.security;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName Channel.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AuthChannel对象", description="三方渠道")
public class AuthChannelVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public AuthChannelVO(String domain,Long id, String dataState, String channelName, String channelLabel, String appId, String appSecret, String otherConfig, String companyNo, String notifyUrl) {
        super(id, dataState);
        this.channelName = channelName;
        this.channelLabel = channelLabel;
        this.appId = appId;
        this.appSecret = appSecret;
        this.otherConfig = otherConfig;
        this.companyNo = companyNo;
        this.notifyUrl = notifyUrl;
        this.domain = domain;
    }

    @ApiModelProperty(value = "通道名称")
    private String channelName;

    @ApiModelProperty(value = "通道唯一标记")
    private String channelLabel;

    @ApiModelProperty(value = "商户appid")
    private String appId;

    @ApiModelProperty(value = "企业三方app秘钥")
    private String appSecret;

    @ApiModelProperty(value = "其他配置")
    private String otherConfig;

    @ApiModelProperty(value = "请求域名")
    private String domain;

    @ApiModelProperty(value = "商户编号")
    private String companyNo;

    @ApiModelProperty(value = "回调地址")
    private String notifyUrl;

    @ApiModelProperty(value = "其他配置")
    private List<OtherConfigVO> otherConfigVOs;


}
