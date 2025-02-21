package com.itheima.bolee.framework.mybatisplus.basic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description：实体基础类
 */
@Data
@NoArgsConstructor
public class BasePojo implements Serializable {

    //主键
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long id;

    //创建时间:INSERT代表只在插入时填充
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    public LocalDateTime createTime;

    //修改时间：INSERT_UPDATE 首次插入、其次更新时填充(或修改)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    public LocalDateTime updateTime;

    //创建人Id：INSERT_UPDATE 首次插入、其次更新时填充(或修改)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createBy;

    //修改人Id:INSERT_UPDATE 首次插入、其次更新时填充(或修改)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long updateBy;

    //是否有效
    @TableField(fill = FieldFill.INSERT)
    public String dataState;

    public BasePojo(Long id, String dataState) {
        this.id = id;
        this.dataState = dataState;
    }
}
