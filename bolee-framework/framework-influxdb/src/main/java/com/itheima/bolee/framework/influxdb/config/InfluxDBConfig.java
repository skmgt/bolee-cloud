package com.itheima.bolee.framework.influxdb.config;


import com.itheima.bolee.framework.influxdb.core.Executor;
import com.itheima.bolee.framework.influxdb.core.ParameterHandler;
import com.itheima.bolee.framework.influxdb.core.ResultSetHandler;
import org.influxdb.InfluxDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 时序数据库配置类
 */
@Configuration
public class InfluxDBConfig {

    @Bean(name = "executor")
    public Executor executor(InfluxDB influxDB) {
        return new Executor(influxDB);
    }

    @Bean(name = "parameterHandler")
    public ParameterHandler parameterHandler(InfluxDB influxDB) {
        return new ParameterHandler();
    }

    @Bean(name = "resultSetHandler")
    public ResultSetHandler resultSetHandler(InfluxDB influxDB) {
        return new ResultSetHandler();
    }
}
