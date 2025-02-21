package com.itheima.bolee.framework.commons.dto.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description：日志模块
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogBusinessVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public LogBusinessVO(Long id, String dataState, String requestId, String host, String hostAddress, String requestUri, String requestMethod, String requestBody, String responseBody, int responseCode, String responseMsg, Long userId, String userName, String businessType, String deviceNumber, List<String> createdTimeQuerty, String companyNO,String sex,String lastReadUrl,String province,String city) {
        super(id, dataState);
        this.requestId = requestId;
        this.host = host;
        this.hostAddress = hostAddress;
        this.requestUri = requestUri;
        this.requestMethod = requestMethod;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.userId = userId;
        this.userName = userName;
        this.businessType = businessType;
        this.deviceNumber = deviceNumber;
        this.createdTimeQuerty = createdTimeQuerty;
        this.companyNO = companyNO;
        this.sex = sex;
        this.lastReadUrl = lastReadUrl;
        this.province = province;
        this.city = city;
    }

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
    private String requestBody;

    @ApiModelProperty(value = "应答body")
    private String responseBody;

    @ApiModelProperty(value = "应答code")
    private int responseCode;

    @ApiModelProperty(value = "应答msg")
    private String responseMsg;

    @ApiModelProperty(value = "用户")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "设备号")
    private String deviceNumber;

    @ApiModelProperty(value = "时间查询")
    private List<String> createdTimeQuerty;

    @ApiModelProperty(value = "企业编号")
    private String companyNO;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "上次浏览页面")
    private String lastReadUrl;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

}