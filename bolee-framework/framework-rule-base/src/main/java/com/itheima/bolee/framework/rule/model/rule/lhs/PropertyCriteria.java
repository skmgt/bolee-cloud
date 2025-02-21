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
package com.itheima.bolee.framework.rule.model.rule.lhs;

import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.framework.rule.model.library.Datatype;
import com.itheima.bolee.framework.rule.model.rule.Op;
import com.itheima.bolee.framework.rule.model.rule.Value;
import com.itheima.bolee.framework.rule.runtime.assertor.AssertorEvaluator;
import com.itheima.bolee.framework.rule.runtime.rete.EvaluationContext;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年6月1日
 */
public class PropertyCriteria {
	private String property;
	private Op op;
	private Value value;
	private String id;
	public String getId(){
		if(id==null){
			id=property+op.name()+value.getId();
		}
		return id;
	}
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		Object left=Utils.getObjectProperty(obj, property);
		Object right=context.getValueCompute().complexValueCompute(value,obj,context,allMatchedObjects,null);
		if(right==null){
			return false;
		}
		AssertorEvaluator assertorEvaluator=context.getAssertorEvaluator();
		Datatype datatype=Utils.getDatatype(left);
		boolean result=assertorEvaluator.evaluate(left, right, datatype,op);
		return result;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Op getOp() {
		return op;
	}
	public void setOp(Op op) {
		this.op = op;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
}
