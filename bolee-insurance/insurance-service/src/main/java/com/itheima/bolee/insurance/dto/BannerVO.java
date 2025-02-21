package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
/**
 * @Description：
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Banner对象", description="")
public class BannerVO extends BaseVO {

    @Builder
    public BannerVO(String bannerType,Long id,String dataState,List<FileVO> fileVOs,Integer sortNo,String url){
        super(id, dataState);
        this.sortNo=sortNo;
        this.url=url;
        this.fileVOs=fileVOs;
        this.bannerType=bannerType;
    }

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "调整路径")
    private String url;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "导航类型（0首页 1推荐）")
    private String bannerType;
}
