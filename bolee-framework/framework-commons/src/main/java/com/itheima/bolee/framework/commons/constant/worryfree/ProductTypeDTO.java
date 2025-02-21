package com.itheima.bolee.framework.commons.constant.worryfree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductType
 *
 * @author: wgl
 * @describe: 省心配产品匹配DTO对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTypeDTO {

    //分类编号
    private String categoryNo;

    //分类键
    private String categoryKey;
}
