package com.itheima.bolee.task.job;

import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ClearFileHandlerJob.java
 * @Description 清理垃圾文件
 */
@Component
public class ClearFileHandlerJob {

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    @XxlJob(value = "clear-file")
    public ReturnT<String> execute(String param) {
        Boolean responseWrap = fileBusinessFeign.clearFile();
        if (responseWrap){
            ReturnT.SUCCESS.setMsg("计划任务：清理垃圾文件-成功");
            return ReturnT.SUCCESS;
        }
        ReturnT.FAIL.setMsg("计划任务：清理垃圾文件-失败");
        return ReturnT.FAIL;

    }
}
