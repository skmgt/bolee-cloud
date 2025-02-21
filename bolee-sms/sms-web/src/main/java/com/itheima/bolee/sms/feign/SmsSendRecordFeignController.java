package com.itheima.bolee.sms.feign;

import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SmsSendRecordController.java
 * @Description 发送记录Controller
 */
@RestController
@RequestMapping("sms-send-record-feign")
@Slf4j
@Api(tags = "发送记录fegin-controller")
public class SmsSendRecordFeignController {

    @Autowired
    ISmsSendRecordService smsSendRecordService;

    @PostMapping("call-back-sms-send-records")
    @ApiOperation(value = "查询需要查询的短信发送",notes = "查询需要查询的短信发送")
    List<SmsSendRecordVO> callBackSmsSendRecords(){
        List<SmsSendRecordVO> smsSendRecords = smsSendRecordService.callBackSmsSendRecords();
        return smsSendRecords;
    }

}
