package com.itheima.bolee.points.service;

import com.itheima.bolee.points.pojo.BusinessLog;

/**
 * @ClassName IBusinessLogService.java
 * @Description 日志服务接口
 */
public interface IBusinessLogService {

    /**
     * 持久化日志信息
     * @param businessLog 日志对象
     * @return 是否执行成功
     */
    Boolean createBusinessLog(BusinessLog businessLog);
}
