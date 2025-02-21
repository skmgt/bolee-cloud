package com.itheima.bolee.insurance.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.insurance.validator.anno.IdentityCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description：客户关系表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CustomerRelation对象", description = "客户关系表")
public class CustomerRelationVO extends BaseVO {

    @Builder
    public CustomerRelationVO(Long id, String dataState, List<FileVO> fileVOs, String relation, String name, String identityCard, String companyNo, Integer sortNo, String socialSecurity, Long customerId, Long createBy, String[] relations,String sex) {
        super(id, dataState);
        this.relation = relation;
        this.name = name;
        this.identityCard = identityCard;
        this.companyNo = companyNo;
        this.sortNo = sortNo;
        this.socialSecurity = socialSecurity;
        this.fileVOs = fileVOs;
        this.customerId = customerId;
        this.relations = relations;
        this.sex = sex;
        setCreateBy(createBy);
    }

    /**
     * 根据身份证号获取性别
     * @return
     */
    public String setSexByIdCard() {
        String idNumber = this.identityCard;
        if(StrUtil.isNotEmpty(idNumber)) {
            if (idNumber.length() == 18) {
                char genderChar = idNumber.charAt(16); // 18位身份证号的第17位
                int genderDigit = Character.getNumericValue(genderChar);
                if (genderDigit % 2 == 0) {
                    this.sex = "女";
                    return "女";
                } else {
                    this.sex = "男";
                    return "男";
                }
            } else if (idNumber.length() == 15) {
                char genderChar = idNumber.charAt(14); // 15位身份证号的最后一位
                int genderDigit = Character.getNumericValue(genderChar);
                if (genderDigit % 2 == 0) {
                    this.sex = "女";
                    return "女";
                } else {
                    this.sex = "男";
                    return "男";
                }
            } else {
                // 如果身份证号位数不是15位或18位，可以根据实际需求返回错误信息或其他处理方式
                this.sex = "无法确定性别";
                return "无法确定性别";
            }
        }else {
            // 如果身份证号位数不是15位或18位，可以根据实际需求返回错误信息或其他处理方式
            this.sex = "无法确定性别";
            return "无法确定性别";
        }
    }


    @ApiModelProperty(value = "关系人ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "身份证号码")
    @IdentityCard(message = "身份证号格式错误")
    private String identityCard;

    @ApiModelProperty(value = "公司")
    private String companyNo;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "社保状态（0有 1无）")
    private String socialSecurity;

    @ApiModelProperty(value = "城市编号")
    private String cityNo;

    @ApiModelProperty(value = "收入")
    private BigDecimal income;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;

    @ApiModelProperty(value = "关系列表")
    private String[] relations;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;
}
