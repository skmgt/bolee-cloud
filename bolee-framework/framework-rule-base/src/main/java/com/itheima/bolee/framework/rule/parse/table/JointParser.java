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
package com.itheima.bolee.framework.rule.parse.table;

import com.itheima.bolee.framework.rule.model.rule.Op;
import com.itheima.bolee.framework.rule.model.table.Condition;
import com.itheima.bolee.framework.rule.model.table.Joint;
import com.itheima.bolee.framework.rule.model.table.JointType;
import com.itheima.bolee.framework.rule.parse.Parser;
import com.itheima.bolee.framework.rule.parse.ValueParser;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class JointParser implements Parser<Joint> {
	private ValueParser valueParser;
	public Joint parse(Element element) {
		Joint joint=new Joint();
		joint.setType(JointType.valueOf(element.attributeValue("type")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("condition")){
				joint.addCondition(parseCondition(ele));
			}else if(support(ele.getName())){
				joint.addJoint(parse(ele));
			}
		}
		return joint;
	}
	public Condition parseCondition(Element element) {
		Condition condition=new Condition();
		condition.setOp(Op.valueOf(element.attributeValue("op")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				condition.setValue(valueParser.parse(ele));
				break;
			}
		}
		return condition;
	}
	public boolean support(String name) {
		return name.equals("joint");
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
}
