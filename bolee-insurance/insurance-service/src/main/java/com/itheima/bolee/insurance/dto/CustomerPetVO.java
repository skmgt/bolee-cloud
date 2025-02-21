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
 * @Description：客户宠物
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CustomerPet对象", description="客户宠物")
public class CustomerPetVO extends BaseVO {

    @Builder
    public CustomerPetVO(Long id,String dataState,List<FileVO> fileVOs,String customerId,String petType,Date birthday,String sex,String sterilization){
        super(id, dataState);
        this.customerId=customerId;
        this.petType=petType;
        this.birthday=birthday;
        this.sex=sex;
        this.sterilization=sterilization;
        this.fileVOs=fileVOs;
    }

    @ApiModelProperty(value = "系统账号编号")
    private String customerId;

    @ApiModelProperty(value = "宠物种类：0猫 1狗")
    private String petType;

    @ApiModelProperty(value = "生日")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    @ApiModelProperty(value = "性别：0公 1母")
    private String sex;

    @ApiModelProperty(value = "绝育情况：0未绝育 1已绝育 3未知")
    private String sterilization;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
