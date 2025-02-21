package com.itheima.bolee.framework.commons.dto.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadMultipartFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名称")
    public String originalFilename;

    @ApiModelProperty(value = "文件数组")
    public byte[] fileByte;

}
