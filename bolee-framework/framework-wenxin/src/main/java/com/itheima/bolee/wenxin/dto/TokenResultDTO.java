package com.itheima.bolee.wenxin.dto;

import lombok.Data;

/**
 * TokenDTO
 *
 * @author: wgl
 * @describe: token对象
 * @date: 2022/12/28 10:10
 */
@Data
public class TokenResultDTO extends RequestResultDTO {

    private String token;
}
