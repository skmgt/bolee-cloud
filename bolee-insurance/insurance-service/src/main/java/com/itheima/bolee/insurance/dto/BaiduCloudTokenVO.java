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
 * BaiduCloudTokenVO
 *
 * @author: wgl
 * @describe: 百度云Token对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="百度云Token对象", description="")
public class BaiduCloudTokenVO extends BaseVO {

    @Builder
    public BaiduCloudTokenVO(Long id, String dataState,String refreshToken, String expiresIn, String accessToken,String sessionKey,String sessionSecret,String scope){
        super(id, dataState);
        this.refreshToken=refreshToken;
        this.expiresIn=expiresIn;
        this.accessToken=accessToken;
        this.sessionKey = sessionKey;
        this.sessionSecret = sessionSecret;
        this.scope = scope;
    }

    @ApiModelProperty(value = "百度云刷新Token")
    private String refreshToken;

    @ApiModelProperty(value = "百度云超时时间")
    private String expiresIn;

    @ApiModelProperty(value = "百度云session_key")
    private String sessionKey;

    @ApiModelProperty(value = "百度云session_secret")
    private String sessionSecret;
    @ApiModelProperty(value = "百度云访问token")
    private String accessToken;

    @ApiModelProperty(value = "百度云scope")
    private String scope;


}