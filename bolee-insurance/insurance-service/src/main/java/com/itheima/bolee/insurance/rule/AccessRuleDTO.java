package com.itheima.bolee.insurance.rule;

import com.itheima.bolee.insurance.dto.DoInsureVo;
import lombok.Data;

/**
 * AccessRuleDTO
 *
 * @author: wgl
 * @describe: 准入规则DTO对象
 * @date: 2022/12/28 10:10
 */
@Data
public class AccessRuleDTO {

    /**
     * 传入对象
     */
    private DoInsureVo data;

    /**
     * 准入结论
     */
    private boolean falg;

    /**
     * 额外新增的钱
     */
    private Double addMoney;

    /**
     * 判断是否准入通过的方法
     *
     * @return
     */
    public boolean isAccess() {
        return this.falg;
    }

}
