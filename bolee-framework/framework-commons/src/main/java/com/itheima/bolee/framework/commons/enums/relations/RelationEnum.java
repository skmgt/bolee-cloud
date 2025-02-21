package com.itheima.bolee.framework.commons.enums.relations;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RelationEnum
 *
 * @author: wgl
 * @describe: 关系枚举类
 * @date: 2022/12/28 10:10
 */
@AllArgsConstructor
@Getter
public enum RelationEnum{
    SELF("0","自己"),
    SPAUSE("1","配偶"),
    PARENTS("2","父母"),
    CHILDREN("3","子女")
    ;
    private String relation;

    private String des;
}
