package com.itheima.bolee.dict.init;

import com.itheima.bolee.dict.service.IDataDictService;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName InitDataDict.java
 * @Description 热加载数字字典
 */
@Component
public class InitDataDict {

    @Autowired
    IDataDictService dataDictService;

    @Async
    @PostConstruct
    public void initDataDict(){
        Timer timer = new Timer();
        timer.schedule(new InitTask(timer),10*1000);
    }

    class InitTask extends TimerTask{

        private Timer timer;

        private InitTask(Timer timer) {
            this.timer= timer;
        }

        @Override
        public void run() {
            //所有ParentKey的set集合
            Set<String> parentKeyAll = dataDictService.findParentKeyAll();
            if (EmptyUtil.isNullOrEmpty(parentKeyAll)){
                return;
            }
            //初始化父亲目录下所有有效状态的数据
            parentKeyAll.forEach(n->{
                dataDictService.findDataDictVOByParentKey(n);
            });
            //所有dataKey的set集合
            Set<String> dataKeyAll = dataDictService.findDataKeyAll();
            if (EmptyUtil.isNullOrEmpty(parentKeyAll)){
                return;
            }
            //初始化datakey对应有效状态的数据
            dataKeyAll.forEach(n->{
                dataDictService.findDataDictVOByDataKey(n);
            });
        }
    }
}
