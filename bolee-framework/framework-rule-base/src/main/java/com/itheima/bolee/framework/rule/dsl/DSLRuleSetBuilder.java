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
package com.itheima.bolee.framework.rule.dsl;

import com.itheima.bolee.framework.rule.RuleException;
import com.itheima.bolee.framework.rule.builder.RulesRebuilder;
import com.itheima.bolee.framework.rule.builder.resource.Resource;
import com.itheima.bolee.framework.rule.dsl.builder.ContextBuilder;
import com.itheima.bolee.framework.rule.model.rule.Library;
import com.itheima.bolee.framework.rule.model.rule.Rule;
import com.itheima.bolee.framework.rule.model.rule.RuleSet;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年2月16日
 */
public class DSLRuleSetBuilder implements ApplicationContextAware{
	public static final String BEAN_ID="urule.dslRuleSetBuilder";
	private Collection<ContextBuilder> contextBuilders;
	private RulesRebuilder rulesRebuilder;
	public RuleSet build(String script) throws IOException{
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(script);
		RuleParserLexer lexer=new RuleParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		RuleParserParser parser=new RuleParserParser(tokenStream);
		ScriptDecisionTableErrorListener errorListener=new ScriptDecisionTableErrorListener();
		parser.addErrorListener(errorListener);
		BuildRulesVisitor visitor=new BuildRulesVisitor(contextBuilders,tokenStream);
		RuleSet ruleSet=visitor.visitRuleSet(parser.ruleSet());
		rebuildRuleSet(ruleSet);
		String error=errorListener.getErrorMessage();
		if(error!=null){
			throw new RuleException("Script parse error:"+error);
		}
		return ruleSet;
	}
	
	private void rebuildRuleSet(RuleSet ruleSet){
		List<Library> libraries=ruleSet.getLibraries();
		List<Rule> rules=ruleSet.getRules();
		rulesRebuilder.rebuildRulesForDSL(libraries, rules);
	}
	
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}


	public boolean support(Resource resource){
		String path=resource.getPath();
		return path.toLowerCase().endsWith(Constant.UL_SUFFIX);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		contextBuilders=applicationContext.getBeansOfType(ContextBuilder.class).values();
	}
}
