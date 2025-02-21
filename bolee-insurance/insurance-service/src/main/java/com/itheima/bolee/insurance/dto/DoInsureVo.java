package com.itheima.bolee.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName DoInsureVo.java
 * @Description 投保请求Vo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value="DoInsureVo对象", description="投保请求Vo")
public class DoInsureVo implements Serializable {

    @ApiModelProperty(value = "保险产品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insuranceId;

    @ApiModelProperty(value = "保险方案ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long insurancePlanId;

    @ApiModelProperty(value = "保险公司编号")
    private String companyNo;

    @ApiModelProperty(value = "被投保人")
    private List<String> customerRelationIds;

    @ApiModelProperty(value = "旅游：保障生效期")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime safeguardStartTime;

    @ApiModelProperty(value = "保险系数IDS")
    private List<Long> insuranceCoefficentIds;

    @ApiModelProperty(value = "理财：购买价格")
    private BigDecimal price;

    @ApiModelProperty(value = "校验规则：0医疗 1重疾 2意外 3养老 4年金 5旅游 6宠物 7定寿")
    private String checkRule;

    @ApiModelProperty(value = "代理人Id")
    private String agentId;

    @ApiModelProperty(value = "代理人姓名")
    private String agentName;

    @ApiModelProperty(value = "是否有问题:0 是，1 否")
    private String isProblem;

    @ApiModelProperty(value = "健康检查传输对象")
    private AdviceHealthDTO adviceHealthDTO;

    @Builder
    public DoInsureVo(String isProblem,Long insuranceId, Long insurancePlanId, String companyNo, List<String> customerRelationIds, LocalDateTime safeguardStartTime, List<Long> insuranceCoefficentIds, BigDecimal price,String checkRule) {
        this.insuranceId = insuranceId;
        this.insurancePlanId = insurancePlanId;
        this.companyNo = companyNo;
        this.customerRelationIds = customerRelationIds;
        this.safeguardStartTime = safeguardStartTime;
        this.insuranceCoefficentIds = insuranceCoefficentIds;
        this.price = price;
        this.checkRule = checkRule;
        this.isProblem = isProblem;
    }
}
