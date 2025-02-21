package com.itheima.bolee.insurance.rule;

import com.itheima.bolee.framework.rule.model.Label;
import lombok.Data;

/**
 * SafeItemDTO
 *
 * @author: wgl
 * @describe: 保障项
 * @date: 2022/12/28 10:10
 */
@Data
public class SafeItemDTO {

    @Label(value = "保险方案ID")
    private Long planId;

    @Label(value = "保障项key：mzyl")
    private String safeguardKey;

    @Label(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @Label(value = "保障项值")
    private String safeguardValue;

    @Label(value = "保障项别名：一般门诊费用")
    private String safeguardAsName;

    @Label(value = "保障类型（0保障规则 1 保障信息）")
    private String sageguardType;

    @Label(value = "显示位置：0 列表页 1 详情页")
    private String position;

    @Label(value = "保障内容补充说明")
    private String remake;

}