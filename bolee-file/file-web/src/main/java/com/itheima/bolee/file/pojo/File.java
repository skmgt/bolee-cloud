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
 * @Description：附件
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_file")
@ApiModel(value="File对象", description="附件")
public class File extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public File(Long id, String dataState, Long businessId, String businessType, String suffix, String fileName, String pathUrl, String storeFlag, String bucketName, String uploadId, String md5, String status, String companyNo) {
        super(id, dataState);
        this.businessId = businessId;
        this.businessType = businessType;
        this.suffix = suffix;
        this.fileName = fileName;
        this.pathUrl = pathUrl;
        this.storeFlag = storeFlag;
        this.bucketName = bucketName;
        this.uploadId = uploadId;
        this.md5 = md5;
        this.status = status;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "后缀名")
    private String suffix;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "访问路径")
    private String pathUrl;

    @ApiModelProperty(value = "存储源标识，参考FileConstant")
    private String storeFlag;

    @ApiModelProperty(value = "存储空间名称")
    private String bucketName;

    @ApiModelProperty(value = "分片上传文件Id")
    private String uploadId;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "状态：上传中【sending】，完成【succeed】，失败【failed】")
    private String status;

    @ApiModelProperty(value = "企业号")
    private String companyNo;

}
