package com.itheima.bolee.rule.console.servlet.knowledge;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.rule.console.servlet.RenderPageServletHandler;
import com.itheima.bolee.framework.rule.runtime.KnowledgePackage;
import com.itheima.bolee.framework.rule.runtime.KnowledgePackageWrapper;
import com.itheima.bolee.framework.rule.runtime.cache.CacheUtils;
import com.itheima.bolee.framework.rule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2016年8月17日
 */
public class LoadKnowledgeServletHandler  extends RenderPageServletHandler{
	private KnowledgePackageService knowledgePackageService;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String packageId=req.getParameter("packageId");
		if(StringUtils.isEmpty(packageId)){
			resp.setContentType("text/html");
			PrintWriter pw=resp.getWriter();
			pw.write("<html>");
			pw.write("<header>");
			pw.write("</header>");
			pw.write("<body>");
			pw.write("<h1>packageId can not be null<h1>");
			pw.write("</body>");
			pw.write("</html>");
			pw.flush();
			pw.close();
			return;
		}
		packageId=Utils.decodeURL(packageId);
		String timestamp=req.getParameter("timestamp");
		KnowledgePackage knowledgePackage=CacheUtils.getKnowledgeCache().getKnowledge(packageId);
		if(knowledgePackage==null){
			knowledgePackage=knowledgePackageService.buildKnowledgePackage(packageId);
			CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
		}
		if(StringUtils.isNotEmpty(timestamp)){
			long remoteTimestamp=Long.valueOf(timestamp);
			long localTimestamp=knowledgePackage.getTimestamp();
			if(localTimestamp>remoteTimestamp){
				writeObjectToJson(resp, new KnowledgePackageWrapper(knowledgePackage));
			}
		}else{
			writeObjectToJson(resp, new KnowledgePackageWrapper(knowledgePackage));
		}
	}
	@Override
	public String url() {
		return "/loadknowledge";
	}
	public void setKnowledgePackageService(KnowledgePackageService knowledgePackageService) {
		this.knowledgePackageService = knowledgePackageService;
	}
}
