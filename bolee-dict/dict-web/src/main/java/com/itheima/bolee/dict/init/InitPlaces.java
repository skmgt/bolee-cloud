package com.itheima.bolee.dict.init;

import com.itheima.bolee.dict.service.IPlacesService;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName InitPlaces.java
 * @Description 热加载区域
 */
@Component
public class InitPlaces {

    @Autowired
    IPlacesService placesService;

    @Async
    @PostConstruct
    public void initPlaces(){
        Timer timer = new Timer();
        timer.schedule(new InitTask(timer),10*1000);
    }

    class InitTask extends TimerTask {

        private Timer timer;

        private InitTask(Timer timer) {
            this.timer= timer;
        }

        @Override
        public void run() {
            //初始化省列表
            List<PlacesVO> provinces = placesService.findPlacesVOListByParentId(SuperConstant.CHINA_CODE);
            if (EmptyUtil.isNullOrEmpty(provinces)){
                return;
            }
            //初始化市列表
            provinces.forEach(n->{
                List<PlacesVO> citys = placesService.findPlacesVOListByParentId(n.getId());
                if (EmptyUtil.isNullOrEmpty(citys)){
                    return;
                }
                //初始化区列表
                citys.forEach(k->{
                    placesService.findPlacesVOListByParentId(k.getId());
                });
            });
        }
    }
}
