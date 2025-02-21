package com.itheima.bolee.framework.commons.dto.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @ClassName SendMessageVO.java
 * @Description 短信发送Vo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SendMessageVO implements Serializable {

    @ApiModelProperty(value = "模板编号")
    String templateNo;

    @ApiModelProperty(value = "签名编号")
    String sginNo;

    @ApiModelProperty(value = "均衡算法")
    String loadBalancerType;

    @ApiModelProperty(value = "手机号码组")
    Set<String> mobiles;

    @ApiModelProperty(value = "模板参数")
    LinkedHashMap<String, String> templateParam;

    @Builder
    public SendMessageVO(String templateNo, String sginNo, String loadBalancerType, Set<String> mobiles, LinkedHashMap<String, String> templateParam) {
        this.templateNo = templateNo;
        this.sginNo = sginNo;
        this.loadBalancerType = loadBalancerType;
        this.mobiles = mobiles;
        this.templateParam = templateParam;
    }
}
