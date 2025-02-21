/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.itheima.bolee.rule.console.servlet.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.itheima.bolee.framework.rule.RuleException;
import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.rule.console.repository.RepositoryResourceProvider;
import com.itheima.bolee.rule.console.repository.RepositoryService;
import com.itheima.bolee.rule.console.servlet.WriteJsonServletHandler;
import com.itheima.bolee.framework.rule.model.library.action.ActionLibrary;
import com.itheima.bolee.framework.rule.model.library.action.SpringBean;
import com.itheima.bolee.framework.rule.parse.deserializer.ActionLibraryDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.ConstantLibraryDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.DecisionTableDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.DecisionTreeDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.Deserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.ParameterLibraryDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.RuleSetDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.ScriptDecisionTableDeserializer;
import com.itheima.bolee.framework.rule.parse.deserializer.VariableLibraryDeserializer;
import com.itheima.bolee.framework.rule.runtime.BuiltInActionLibraryBuilder;

/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public class XmlServletHandler extends WriteJsonServletHandler implements ApplicationContextAware{
	private RepositoryService repositoryService;
	private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;
	protected List<Deserializer<?>> deserializers=new ArrayList<Deserializer<?>>();
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String methodName=retriveMethod(req);
		if(methodName!=null){
			invokeMethod(methodName, req, resp);
		}else{
			//default load xml
			loadXml(req, resp);
		}
	}
	
	public void loadXml(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Object> result=new ArrayList<Object>();
		String files=req.getParameter("files");
		boolean isaction=false;
		if(files!=null){
			if(files.startsWith("builtinactions")){
				isaction=true;
			}else{
				files=Utils.decodeURL(files);
				String[] paths=files.split(";");
				for(String path:paths){
					path=Utils.toUTF8(path);
					if(path.startsWith(RepositoryResourceProvider.JCR)){
						path=path.substring(4,path.length());					
					}
					String[] subpaths=path.split(",");
					path=subpaths[0];
					String version=null;
					if(subpaths.length==2){
						version=subpaths[1];
					}
					try{
						InputStream inputStream=null;
						if(StringUtils.isEmpty(version)){
							inputStream=repositoryService.readFile(path,null);
						}else{
							inputStream=repositoryService.readFile(path,version);			
						}
						try{
							Element element=parseXml(inputStream);
							for(Deserializer<?> des:deserializers){
								if(des.support(element)){
									result.add(des.deserialize(element));
									if(des instanceof ActionLibraryDeserializer){
										isaction=true;
									}
									break;
								}
							}
						}finally{
							inputStream.close();
						}
					}catch(Exception ex){
						throw new RuleException(ex);
					}
				}
			}
		}
		if(isaction){
			List<SpringBean> beans=builtInActionLibraryBuilder.getBuiltInActions();
			if(beans.size()>0){
				ActionLibrary al=new ActionLibrary();
				al.setSpringBeans(beans);
				result.add(al);
			}
		}
		writeObjectToJson(resp,result);
	}
	
	protected Element parseXml(InputStream stream){
		SAXReader reader=new SAXReader();
		Document document;
		try {
			document = reader.read(stream);
			Element root=document.getRootElement();
			return root;
		} catch (DocumentException e) {
			throw new RuleException(e);
		}
	}

	@Override
	public String url() {
		return "/xml";
	}
	
	public void setBuiltInActionLibraryBuilder(
			BuiltInActionLibraryBuilder builtInActionLibraryBuilder) {
		this.builtInActionLibraryBuilder = builtInActionLibraryBuilder;
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		ActionLibraryDeserializer actionLibraryDeserializer=(ActionLibraryDeserializer)applicationContext.getBean(ActionLibraryDeserializer.BEAN_ID);
		VariableLibraryDeserializer variableLibraryDeserializer=(VariableLibraryDeserializer)applicationContext.getBean(VariableLibraryDeserializer.BEAN_ID);
		ConstantLibraryDeserializer constantLibraryDeserializer=(ConstantLibraryDeserializer)applicationContext.getBean(ConstantLibraryDeserializer.BEAN_ID);
		RuleSetDeserializer ruleSetDeserializer=(RuleSetDeserializer)applicationContext.getBean(RuleSetDeserializer.BEAN_ID);
		DecisionTableDeserializer decisionTableDeserializer=(DecisionTableDeserializer)applicationContext.getBean(DecisionTableDeserializer.BEAN_ID);
		ScriptDecisionTableDeserializer scriptDecisionTableDeserializer=(ScriptDecisionTableDeserializer)applicationContext.getBean(ScriptDecisionTableDeserializer.BEAN_ID);
		DecisionTreeDeserializer decisionTreeDeserializer=(DecisionTreeDeserializer)applicationContext.getBean(DecisionTreeDeserializer.BEAN_ID);
		ParameterLibraryDeserializer parameterLibraryDeserializer=(ParameterLibraryDeserializer)applicationContext.getBean(ParameterLibraryDeserializer.BEAN_ID);
		deserializers.add(actionLibraryDeserializer);
		deserializers.add(variableLibraryDeserializer);
		deserializers.add(constantLibraryDeserializer);
		deserializers.add(ruleSetDeserializer);
		deserializers.add(decisionTableDeserializer);
		deserializers.add(scriptDecisionTableDeserializer);
		deserializers.add(decisionTreeDeserializer);
		deserializers.add(parameterLibraryDeserializer);
	}
}
