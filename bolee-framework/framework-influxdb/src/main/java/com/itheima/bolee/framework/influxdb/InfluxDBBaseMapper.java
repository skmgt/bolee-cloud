package com.itheima.bolee.framework.influxdb;

import com.itheima.bolee.framework.influxdb.anno.Insert;

import java.util.List;

public interface InfluxDBBaseMapper<T> {

    @Insert
    void insertOne(T entity);

    @Insert
    void insertBatch(List<T> entityList);
}
