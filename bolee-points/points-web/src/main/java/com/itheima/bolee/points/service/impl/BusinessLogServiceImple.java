package com.itheima.bolee.points.service.impl;

import com.itheima.bolee.framework.commons.enums.log.LogBusinessEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.points.mapper.BusinessLogMapper;
import com.itheima.bolee.points.pojo.BusinessLog;
import com.itheima.bolee.points.service.IBusinessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @ClassName BusinessLogServiceImple.java
 * @Description 日志服务接口
 */
@Slf4j
@Service
public class BusinessLogServiceImple implements IBusinessLogService {

    @Autowired
    private BusinessLogMapper businessLogMapper;

    @Override
    public Boolean createBusinessLog(BusinessLog businessLog) {
        try {
            //插入到influxDB中
            businessLogMapper.insertOne(businessLog);
            return true;
        } catch (Exception e) {
            log.error("数据埋点日志插入异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(LogBusinessEnum.SAVE_FAIL);
        }
    }
}
