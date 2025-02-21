package com.itheima.bolee.insurance.rule;

import cn.hutool.core.util.IdcardUtil;
import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ApplicantInfo
 *
 * @author: wgl
 * @describe: 申请人信息
 * @date: 2022/12/28 10:10
 */
@Data
public class ApplicantInfo {

    @Label("申请人姓名")
    private String name;

    @Label("投保人与被投保人关系")
    private String relation;
    @Label("身份证号")
    private String idCard;

    @Label("年龄")
    private int age;

    @Label("收入")
    private BigDecimal income;

    @Label("负债")
    private BigDecimal liabilities;

    @Label("是否有房")
    private boolean hasHouse;

    @Label("是否有车")
    private boolean hasCar;

    @Label("声明疾病")
    private List<String> sicks;

    @Label("职业")
    private String jobNo;

    @Label("常驻城市")
    private String cityNo;

    @Label("子女数量")
    private int childNum;

    @Label("赡养父母数量")
    private int supportingParents;

    @Label("关联风险项")
    private List<String> risks;

    /**
     * 设置身份证号
     */
    public void setIdCard(String idCard){
        this.idCard = idCard;
        setAgeByIdcard(idCard);
    }

    /**
     * 设置年龄
     */
    public void setAgeByIdcard(String idCardNumber){
        // 获取年龄
        this.age = IdcardUtil.getAgeByIdCard(idCardNumber);
    }
    /**
     * 设置年龄
     */
    public void setAge(Integer age){
        this.age = age;
    }
}