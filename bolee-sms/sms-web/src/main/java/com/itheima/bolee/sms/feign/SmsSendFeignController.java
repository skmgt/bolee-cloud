package com.itheima.bolee.sms.feign;

import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.service.ISmsSendService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SmsSendController.java
 * @Description 短信发送controller
 */
@RestController
@RequestMapping("sms-send-feign")
public class SmsSendFeignController {

    @Autowired
    ISmsSendService smsSendService;

    /***
     * @description 发送短信:削峰方式
     * @param sendMessageVO 发送对象
     */
    @PostMapping("send-sms-mq")
    @ApiOperation(value = "发送短信：削峰方式",notes = "发送短信：削峰方式")
    @ApiImplicitParam(name = "sendMessageVO",value = "短信发送对象",required = true,dataType = "SendMessageVO")
    Boolean sendSmsForMq(@RequestBody SendMessageVO sendMessageVO){
        Boolean flag = smsSendService.sendSmsForMq(sendMessageVO);
        return flag;
    }

    /***
     * @description 发送短信:非削峰方式
     * @param sendMessageVO 发送对象
     */
    @PostMapping("send-sms")
    @ApiOperation(value = "发送短信：非削峰方式",notes = "发送短信：非削峰方式")
    @ApiImplicitParam(name = "sendMessageVO",value = "短信发送对象",required = true,dataType = "SendMessageVO")
    Boolean sendSms(@RequestBody SendMessageVO sendMessageVO){
        Boolean flag = smsSendService.sendSms(sendMessageVO);
        return flag;
    }

    /***
     * @description 查询短信发送情况
     * @param smsSendRecordVO 发送记录
     * @return
     */
    @PostMapping("query-send-sms")
    @ApiOperation(value = "查询短信发送情况",notes = "查询短信发送情况")
    @ApiImplicitParam(name = "smsSendRecordVO",value = "短信发送记录",required = true,dataType = "SmsSendRecordVO")
    Boolean querySendSms(@RequestBody SmsSendRecordVO smsSendRecordVO){
        Boolean flag = smsSendService.querySendSms(smsSendRecordVO);
        return flag;
    }

    /***
     * @description 重试发送
     * @param smsSendRecordVO
     * @return
     */
    @PostMapping("retry-send-sms")
    @ApiOperation(value = "重试发送",notes = "重试发送")
    @ApiImplicitParam(name = "smsSendRecordVO",value = "短信发送记录",required = true,dataType = "SmsSendRecordVO")
    Boolean retrySendSms(@RequestBody SmsSendRecordVO smsSendRecordVO){
        Boolean flag = smsSendService.retrySendSms(smsSendRecordVO);
        return flag;
    }
}
