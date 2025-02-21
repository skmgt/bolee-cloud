package com.itheima.bolee.framework.commons.dto.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Proof.java
 * @Description 证明文件对象
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ProofVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder
    public ProofVO(String proofImage, String proofType) {
        this.proofImage = proofImage;
        this.proofType = proofType;
    }

    @ApiModelProperty(value = "签名对应的资质证明图片需先进行 base64编码格式转换")
    private String proofImage;

    @ApiModelProperty(value = "签名证明文件类型")
    private String proofType;

}
