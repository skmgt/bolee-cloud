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
package com.itheima.bolee.framework.rule.dsl.builder;

import com.itheima.bolee.framework.rule.RuleException;
import com.itheima.bolee.framework.rule.dsl.DSLUtils;
import com.itheima.bolee.framework.rule.dsl.RuleParserParser.*;
import com.itheima.bolee.framework.rule.model.rule.Op;
import com.itheima.bolee.framework.rule.model.rule.lhs.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年8月15日
 */
public class NamedConditionBuilder {
	public CriteriaUnit buildNamedCriteria(NamedConditionContext namedConditionContext,String variableCategory){
		CriteriaUnit unit=null;
		if(namedConditionContext instanceof MultiNamedConditionsContext){
			unit=visitMultiNamedConditions((MultiNamedConditionsContext)namedConditionContext, variableCategory);
		}else if(namedConditionContext instanceof SingleNamedConditionsContext){
			unit=visitSingleNamedConditions((SingleNamedConditionsContext)namedConditionContext, variableCategory);
		}else if(namedConditionContext instanceof ParenNamedConditionsContext){
			unit=visitParenNamedConditions((ParenNamedConditionsContext)namedConditionContext, variableCategory);
		}else{
			throw new RuleException("Unsupport context : +namedConditionContext+");
		}
		return unit;
	}
	
	private CriteriaUnit visitSingleNamedConditions(SingleNamedConditionsContext ctx,String variableCategory) {
		Criteria criteria=new Criteria();
		VariableLeftPart leftPart=new VariableLeftPart();
		Left left=new Left();
		left.setLeftPart(leftPart);
		left.setType(LeftType.NamedReference);
		criteria.setLeft(left);
		String variableName=ctx.property().getText();
		leftPart.setVariableLabel(variableName);
		leftPart.setVariableCategory(variableCategory);
		Op op=DSLUtils.parseOp(ctx.op());
		criteria.setOp(op);
		if(ctx.complexValue()==null){
			if(op.equals(Op.Equals)){
				criteria.setOp(Op.Null);
			}else if(op.equals(Op.NotEquals)){
				criteria.setOp(Op.NotNull);				
			}else{
				throw new RuleException("'null' value only support '==' or '!=' operator.");
			}
		}else{
			criteria.setValue(BuildUtils.buildValue(ctx.complexValue()));		
		}
		CriteriaUnit unit=new CriteriaUnit();
		unit.setCriteria(criteria);
		return unit;
	}
	private CriteriaUnit visitMultiNamedConditions(MultiNamedConditionsContext ctx,String variableCategory) {
		List<CriteriaUnit> nextUnits=new ArrayList<CriteriaUnit>();
		List<NamedConditionContext> namedConditions=ctx.namedCondition();
		if(namedConditions!=null){
			for(int i=0;i<namedConditions.size();i++){
				NamedConditionContext context=namedConditions.get(i);
				CriteriaUnit nextUnit=buildNamedCriteria(context, variableCategory);
				nextUnits.add(nextUnit);
				JoinContext joinContext=ctx.join(i);
				if(joinContext!=null){
					if(joinContext.AND()!=null){
						nextUnit.setJunctionType(JunctionType.and);
					}else{
						nextUnit.setJunctionType(JunctionType.or);				
					}
				}
			}
		}
		CriteriaUnit unit=new CriteriaUnit();
		unit.setNextUnits(nextUnits);
		return unit;
	}
	
	private CriteriaUnit visitParenNamedConditions(ParenNamedConditionsContext ctx,String variableCategory) {
		NamedConditionContext namedConditionContext=ctx.namedCondition();
		CriteriaUnit unit=buildNamedCriteria(namedConditionContext, variableCategory);
		return unit;
	}
}
