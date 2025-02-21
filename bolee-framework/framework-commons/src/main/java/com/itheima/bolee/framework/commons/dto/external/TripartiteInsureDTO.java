package com.itheima.bolee.framework.commons.dto.external;

import lombok.Data;

/**
 * TripartiteInsureDTO
 *
 * @author: wgl
 * @describe: 投保交互DTO
 * @date: 2022/12/28 10:10
 */
@Data
public class TripartiteInsureDTO {

    /**
     * 图片浏览地址
     */
    private String url;

    /**
     * 允许投保标志位
     */
    private Boolean flag;
}
