package com.itheima.bolee.file.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_file_part")
@ApiModel(value="FilePart对象", description="分片上传")
public class FilePart extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public FilePart(Long id, String dataState, String uploadId, Integer partNumber, String uploadResult, String md5, String bucketName, String fileName, String storeFlag, String companyNo) {
        super(id, dataState);
        this.uploadId = uploadId;
        this.partNumber = partNumber;
        this.uploadResult = uploadResult;
        this.md5 = md5;
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.storeFlag = storeFlag;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "唯一上传id")
    private String uploadId;

    @ApiModelProperty(value = "当前片数")
    private Integer partNumber;

    @ApiModelProperty(value = "分片上传结果(json)")
    private String uploadResult;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "存储空间名称")
    private String bucketName;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "存储源标识，参考FileConstant")
    private String storeFlag;

    @ApiModelProperty(value = "企业号")
    private String companyNo;

}
