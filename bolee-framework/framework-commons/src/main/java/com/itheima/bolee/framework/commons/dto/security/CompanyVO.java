package com.itheima.bolee.framework.commons.dto.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description：企业账号管理
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Company对象", description="企业账号管理")
public class CompanyVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public CompanyVO(Long id, String dataState, String companyNo, String companName, String registeredNo, String province, String area, String city, String address, String status, LocalDateTime expireTime, String webSite, String appSite, String email, String tel, String leaderMobile, String leaderName, List<AuthChannelVO> authChannelVOs,Long[] checkIds) {
        super(id, dataState);
        this.companyNo = companyNo;
        this.companName = companName;
        this.registeredNo = registeredNo;
        this.province = province;
        this.area = area;
        this.city = city;
        this.address = address;
        this.status = status;
        this.expireTime = expireTime;
        this.webSite = webSite;
        this.appSite = appSite;
        this.email = email;
        this.tel = tel;
        this.leaderMobile = leaderMobile;
        this.leaderName = leaderName;
        this.authChannelVOs = authChannelVOs;
        this.checkIds = checkIds;
    }

    @ApiModelProperty(value = "商户编号")
    private String companyNo;

    @ApiModelProperty(value = "企业名称")
    private String companName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String registeredNo;

    @ApiModelProperty(value = "地址(省)")
    private String province;

    @ApiModelProperty(value = "地址(区)")
    private String area;

    @ApiModelProperty(value = "地址(市)")
    private String city;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "状态(停用：0，试用：1，，正式:2)")
    private String status;

    @ApiModelProperty(value = "到期时间 (试用下是默认七天后到期，状态改成停用)")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "商户门店web站点")
    private String webSite;

    @ApiModelProperty(value = "商户app站点")
    private String appSite;

    @ApiModelProperty(value = "联系邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "负责人手机")
    private String leaderMobile;

    @ApiModelProperty(value = "负责人姓名")
    private String leaderName;

    @ApiModelProperty(value = "三方授权渠道")
    private List<AuthChannelVO> authChannelVOs;

    @ApiModelProperty(value = "批量操作id集合")
    private Long[] checkIds;

}
