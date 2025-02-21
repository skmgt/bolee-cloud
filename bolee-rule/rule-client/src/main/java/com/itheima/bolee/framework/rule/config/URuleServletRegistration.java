package com.itheima.bolee.framework.rule.config;

import com.itheima.bolee.framework.rule.KnowledgePackageReceiverServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class URuleServletRegistration {
	
	@Bean
	public ServletRegistrationBean registerURuleServlet(){
		return new ServletRegistrationBean(new KnowledgePackageReceiverServlet(),"/knowledgepackagereceiver");
	}
}

