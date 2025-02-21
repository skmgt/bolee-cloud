package com.itheima.bolee.wenxin.config;

import com.itheima.bolee.wenxin.WenXinGptServiceImpl;
import com.itheima.bolee.wenxin.WenXinService;
import com.itheima.bolee.wenxin.chains.TotalChain;
import com.itheima.bolee.wenxin.chains.impl.QuestionChain;
import com.itheima.bolee.wenxin.chains.impl.TokenChain;
import com.itheima.bolee.wenxin.manage.TokenManage;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WenXinConfgInit
 *
 * @author: wgl
 * @describe: 文心一眼统一配置类
 * @date: 2022/12/28 10:10
 */
@Configuration
public class WenXinConfgInit implements ApplicationContextAware {

    @Bean
    @ConditionalOnMissingBean
    public TotalChain initTtoalChain() {
        return new TotalChain();
    }

    @Bean
    @ConditionalOnMissingBean
    public QuestionChain initQuestionChain() {
        return new QuestionChain();

    }

    @Bean
    @ConditionalOnMissingBean
    public TokenChain initTokenChain() {
        return new TokenChain();
    }

    @Bean
    @ConditionalOnMissingBean
    public WenXinGtpSourceConfig configInit(){
        return new WenXinGtpSourceConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public WenXinService initService(){
        return new WenXinGptServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenManage initManage(){
        return new TokenManage();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TotalChain totalChain = applicationContext.getBean(TotalChain.class);
        TokenChain tokenChain = applicationContext.getBean(TokenChain.class);
        QuestionChain questionChain = applicationContext.getBean(QuestionChain.class);
        totalChain.addChain(tokenChain);
        totalChain.addChain(questionChain);
    }
}
