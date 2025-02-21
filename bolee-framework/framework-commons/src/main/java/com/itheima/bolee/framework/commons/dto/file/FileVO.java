package com.itheima.bolee.framework.commons.dto.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.constant.file.FileConstant;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description：附件表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public FileVO(Long id, String dataState, Long businessId, String businessType, String suffix, String fileName, String pathUrl, String storeFlag, String bucketName, String base64Image, String uploadId, Boolean autoCatalog, String md5, List<String> partETags, String status, String companyNo) {
        super(id, dataState);
        this.businessId = businessId;
        this.businessType = businessType;
        this.suffix = suffix;
        this.fileName = fileName;
        this.pathUrl = pathUrl;
        this.storeFlag = storeFlag;
        this.bucketName = bucketName;
        this.base64Image = base64Image;
        this.uploadId = uploadId;
        this.autoCatalog = autoCatalog;
        this.md5 = md5;
        this.partETags = partETags;
        this.status = status;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "业务ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long businessId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "后缀名")
    private String suffix;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "访问路径")
    private String pathUrl;

    @ApiModelProperty(value = "存储标识（不同的存储标识参考FileConstant）,现支持 aliyunoss、qiniu ")
    private String storeFlag;

    @ApiModelProperty(value = "存储空间名称")
    private String bucketName;

    @ApiModelProperty(value = "base64图片")
    private String base64Image;

    @ApiModelProperty(value = "分片上传文件Id")
    private String uploadId;

    @ApiModelProperty(value = "是否自动生成文件存储目录,如果在storeFilename指定了目录，此值设置为false")
    private Boolean autoCatalog;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "分片上传分片信息")
    private List<String> partETags;

    @ApiModelProperty(value = "上传状态")
    private String status;

    @ApiModelProperty(value = "企业号")
    private String companyNo;

    @ApiModelProperty(value = "历史上传:0 历史有此文件 1、历史无此文件")
    private String isHistory;

}
