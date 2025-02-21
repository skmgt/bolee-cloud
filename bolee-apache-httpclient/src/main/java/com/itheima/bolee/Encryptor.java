package com.itheima.bolee;

import com.itheima.bolee.dto.EncodeDTO;

import java.io.IOException;

public interface Encryptor {


    /***
     * @description 加密body体消息的方法
     * @param body 请求消息体
     * @return: boolean 校验结果
     */
    boolean encode(EncodeDTO body) throws IOException;
}
