package com.itheima.bolee.sms.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：模板表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_sms_template")
@ApiModel(value="SmsTemplate对象", description="模板表")
public class SmsTemplate extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public SmsTemplate(Long id, String dataState, String channelLabel, String templateName, String smsType,
                       String templateNo, String templateCode, String content, String otherConfig,
                       String international, String remark, String acceptStatus, String acceptMsg,
                       String auditStatus, String auditMsg,String companyNo) {
        super(id, dataState);
        this.channelLabel = channelLabel;
        this.templateName = templateName;
        this.smsType = smsType;
        this.templateNo = templateNo;
        this.templateCode = templateCode;
        this.content = content;
        this.otherConfig = otherConfig;
        this.international = international;
        this.remark = remark;
        this.acceptStatus = acceptStatus;
        this.acceptMsg = acceptMsg;
        this.auditStatus = auditStatus;
        this.auditMsg = auditMsg;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "通道唯一标识")
    private String channelLabel;

    @ApiModelProperty(value = "魔板名称")
    private String templateName;

    @ApiModelProperty(value = "短信类型")
    private String smsType;

    @ApiModelProperty(value = "应用模板编号：多通道编号相同则认为是一个模板多个通道公用")
    private String templateNo;

    @ApiModelProperty(value = "三方应用模板code")
    private String templateCode;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "变量配置")
    private String otherConfig;

    @ApiModelProperty(value = "是否国际/港澳台短信")
    private String international;

    @ApiModelProperty(value = "短信申请说明")
    private String remark;

    @ApiModelProperty(value = "是否受理成功")
    private String acceptStatus;

    @ApiModelProperty(value = "受理返回信息")
    private String acceptMsg;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核信息")
    private String auditMsg;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;
}
