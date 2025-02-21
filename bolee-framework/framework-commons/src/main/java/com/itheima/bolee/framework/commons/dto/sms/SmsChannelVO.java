package com.itheima.bolee.framework.commons.dto.sms;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 短信渠道
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmsChannelVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public SmsChannelVO(Long id, String dataState, String channelName, String channelLabel, String channelType, String domain, String accessKeyId, String accessKeySecret, String otherConfig, String level, String remark, String[] checkedIds, String companyNo, List<OtherConfigVO> otherConfigs) {
        super(id, dataState);
        this.channelName = channelName;
        this.channelLabel = channelLabel;
        this.channelType = channelType;
        this.domain = domain;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.otherConfig = otherConfig;
        this.level = level;
        this.remark = remark;
        this.checkedIds = checkedIds;
        this.companyNo = companyNo;
        this.otherConfigs = otherConfigs;
    }

    @ApiModelProperty(value = "通道名称")
    private String channelName;

    @ApiModelProperty(value = "通道唯一标记")
    private String channelLabel;

    @ApiModelProperty(value = "通道类型，1：文字，2：语音，3：推送")
    private String channelType;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "秘钥id")
    private String accessKeyId;

    @ApiModelProperty(value = "秘钥值")
    private String accessKeySecret;

    @ApiModelProperty(value = "其他配置")
    private String otherConfig;

    @ApiModelProperty(value = "优先级")
    private String level;

    @ApiModelProperty(value = "短信申请说明")
    private String remark;

    @ApiModelProperty(value = "选中节点")
    private String[] checkedIds;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "扩展配置",dataType = "OtherConfigVO")
    private List<OtherConfigVO> otherConfigs;

}
