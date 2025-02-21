package com.itheima.bolee.framework.rule.template;

import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.framework.rule.config.SpringApplicationAware;
import com.itheima.bolee.framework.rule.runtime.KnowledgePackage;
import com.itheima.bolee.framework.rule.runtime.KnowledgeSession;
import com.itheima.bolee.framework.rule.runtime.KnowledgeSessionFactory;
import com.itheima.bolee.framework.rule.runtime.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * RuleTemplate
 *
 * @author: wgl
 * @describe: 规则调用模板
 * @date: 2022/12/28 10:10
 */
@Service
public class RuleTemplate {



    /**
     * 获取知识库里的包
     *
     * @param serverName
     * @param packageId
     * @return
     * @throws IOException
     */
    public KnowledgePackage getPackage(String serverName, String packageId) throws IOException {
        //创建一个KnowledgeSession对象
        KnowledgeService knowledgeService = (KnowledgeService) SpringApplicationAware.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge(serverName + "/" + packageId);
        return knowledgePackage;
    }

    /**
     * 获取urule的session连接
     *
     * @param serverName
     * @param packageId
     * @return
     * @throws IOException
     */
    public KnowledgeSession getSession(String serverName, String packageId) throws IOException {
        KnowledgeService knowledgeService = (KnowledgeService) SpringApplicationAware.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge(serverName + "/" + packageId);
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        return session;
    }

    /**
     * 获取urule的session连接
     *
     * @param serverName
     * @param packageId
     * @return
     * @throws IOException
     */
    public void fireRules(Map data, String serverName, String packageId) throws IOException {
        Object knowledgeService = Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = ((KnowledgeService)knowledgeService).getKnowledge(serverName + "/" + packageId);
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        session.fireRules(data);
    }
}