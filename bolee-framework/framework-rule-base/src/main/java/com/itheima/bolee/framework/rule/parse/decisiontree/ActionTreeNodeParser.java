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
package com.itheima.bolee.framework.rule.parse.decisiontree;

import com.itheima.bolee.framework.rule.action.Action;
import com.itheima.bolee.framework.rule.model.decisiontree.ActionTreeNode;
import com.itheima.bolee.framework.rule.model.decisiontree.TreeNodeType;
import com.itheima.bolee.framework.rule.parse.ActionParser;
import com.itheima.bolee.framework.rule.parse.Parser;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class ActionTreeNodeParser implements Parser<ActionTreeNode>,ApplicationContextAware {
	private Collection<ActionParser> actionParsers;
	@Override
	public ActionTreeNode parse(Element element) {
		ActionTreeNode node=new ActionTreeNode();
		node.setNodeType(TreeNodeType.action);
		List<Action> actions=new ArrayList<Action>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			
			for(ActionParser actionParser:actionParsers){
				if(actionParser.support(name)){
					actions.add(actionParser.parse(ele));
					break;
				}
			}
		}
		node.setActions(actions);
		return node;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("action-tree-node");
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		actionParsers=context.getBeansOfType(ActionParser.class).values();
	}
}
