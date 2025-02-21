package com.itheima.bolee.points.feign;

import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.points.pojo.BusinessLog;
import com.itheima.bolee.points.service.IBusinessLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName BusinessReportFeignController.java
 * @Description 业务统计
 */
@Slf4j
@Api(tags = "持久化：业务收集")
@RestController
@RequestMapping("business-log-feign")
public class BusinessLogFeignController {

    @Autowired
    IBusinessLogService businessLogService;

    /**
     * 持久化：日志信息
     * @param logBusinessVO 日志对象
     * @return 是否执行成功
     */
    @PostMapping("create-business-log")
    @ApiOperation(value = "日志信息", notes = "日志信息")
    @ApiImplicitParam(name = "logBusinessVO", value = "日志对象", required = true, dataType = "LogBusinessVO")
    public Boolean createBusinessLog(@RequestBody LogBusinessVO logBusinessVO) {
        return businessLogService.createBusinessLog(BeanConv.toBean(logBusinessVO, BusinessLog.class));
    }
}
