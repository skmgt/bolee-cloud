package com.itheima.bolee.framework.rabbitmq.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description：Rabbit消息封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqMessage implements Serializable {

    /**消息id**/
    private Long id;

    /**标题**/
    private String title;

    /**消息内容**/
    private String content;

    /**业务类型**/
    private String messageType;

    /**产生时间**/
    private Timestamp produceTime;

    /**发送人**/
    private String sender;
}
