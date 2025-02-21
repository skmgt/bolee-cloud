package com.itheima.bolee.framework.commons.dto.click;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClickDTO
 *
 * @author: wgl
 * @describe: 点击对象
 * @date: 2022/12/28 10:10
 */
@Data
public class ClickDTO {

    @ApiModelProperty(value = "请求id")
    private String requestId;

    @ApiModelProperty(value = "域名")
    private String host;

    @ApiModelProperty(value = "ip地址")
    private String hostAddress;

    @ApiModelProperty(value = "请求路径")
    private String requestUri;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "请求body")
    private String requesBody;

    @ApiModelProperty(value = "应答body")
    private String responseBody;

    @ApiModelProperty(value = "应答code")
    private String responseCode;

    @ApiModelProperty(value = "应答msg")
    private String responseMsg;

    @ApiModelProperty(value = "用户")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "设备号")
    private String deviceNumber;

    @ApiModelProperty(value = "企业编号")
    private String companyNO;

}