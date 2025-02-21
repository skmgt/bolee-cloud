package com.itheima.bolee.security.init;

import com.itheima.bolee.framework.commons.constant.security.CompanyCacheConstant;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.security.service.ICompanyService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName initCompanyWebSIteInfo.java
 * @Description 初始化企业信息到redis中进行热加载
 */
@Component
public class InitCompany {

    @Autowired
    ICompanyService companyService;

    @Autowired
    RedissonClient redissonClient;

    @Async
    @PostConstruct
    public void initDataDict(){
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
            //查找正式，适用且未有效所有企业
            List<CompanyVO> companyVOs = companyService.findCompanyVOValidation();
            //处理缓存
            if (!EmptyUtil.isNullOrEmpty(companyVOs)){
                companyVOs.forEach(companyVO -> {
                    String webSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getWebSite();
                    RBucket<CompanyVO> webSiteBucket = redissonClient.getBucket(webSiteKey);
                    String appSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getAppSite();
                    RBucket<CompanyVO> appSiteBucket = redissonClient.getBucket(appSiteKey);
                    Duration between = Duration.between(LocalDateTime.now(), companyVO.getExpireTime());
                    if (between.toSeconds() > 0) {
                        webSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
                        appSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
                    }
                });
            }
        }
    }

    /***
     * @description 添加缓存中的站点
     * @param companyVO 企业号
     * @return:
     */
    public void addWebSiteforRedis(CompanyVO companyVO){
        String webSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getWebSite();
        RBucket<CompanyVO> webSiteBucket = redissonClient.getBucket(webSiteKey);
        String appSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getAppSite();
        RBucket<CompanyVO> appSiteBucket = redissonClient.getBucket(appSiteKey);
        Duration between = Duration.between(LocalDateTime.now(), companyVO.getExpireTime());
        if (between.toSeconds() > 0) {
            webSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
            appSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
        }
    }

    /***
     * @description 移除缓存中的站点
     * @param companyVO 企业号
     * @return:
     */
    public void deleteWebSiteforRedis( CompanyVO companyVO){
        String webSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getWebSite();
        RBucket<CompanyVO> webSiteBucket = redissonClient.getBucket(webSiteKey);
        String appSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getAppSite();
        RBucket<CompanyVO> appSiteBucket = redissonClient.getBucket(appSiteKey);
        webSiteBucket.delete();
        appSiteBucket.delete();
    }


    /***
     * @description 更新缓存中的站点
     * @param companyVO 企业号
     * @return:
     */
    public void updataWebSiteforRedis(CompanyVO companyVO){
        String webSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getWebSite();
        RBucket<CompanyVO> webSiteBucket = redissonClient.getBucket(webSiteKey);
        String appSiteKey = CompanyCacheConstant.WEBSITE + companyVO.getAppSite();
        RBucket<CompanyVO> appSiteBucket = redissonClient.getBucket(appSiteKey);
        Duration between = Duration.between(LocalDateTime.now(), companyVO.getExpireTime());
        if (between.toSeconds() > 0) {
            webSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
            appSiteBucket.set(companyVO, between.toSeconds(), TimeUnit.SECONDS);
        }
    }
}
