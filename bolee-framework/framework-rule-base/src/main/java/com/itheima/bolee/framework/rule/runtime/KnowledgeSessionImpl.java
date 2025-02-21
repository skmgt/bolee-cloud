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
package com.itheima.bolee.framework.rule.runtime;

import com.itheima.bolee.framework.rule.RuleException;
import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.framework.rule.debug.DebugWriter;
import com.itheima.bolee.framework.rule.debug.MessageItem;
import com.itheima.bolee.framework.rule.model.GeneralEntity;
import com.itheima.bolee.framework.rule.model.flow.FlowDefinition;
import com.itheima.bolee.framework.rule.model.flow.ins.FlowContextImpl;
import com.itheima.bolee.framework.rule.model.flow.ins.ProcessInstance;
import com.itheima.bolee.framework.rule.model.library.Datatype;
import com.itheima.bolee.framework.rule.model.rule.Rule;
import com.itheima.bolee.framework.rule.runtime.agenda.ActivationImpl;
import com.itheima.bolee.framework.rule.runtime.agenda.Agenda;
import com.itheima.bolee.framework.rule.runtime.agenda.AgendaFilter;
import com.itheima.bolee.framework.rule.runtime.agenda.RuleBox;
import com.itheima.bolee.framework.rule.runtime.event.*;
import com.itheima.bolee.framework.rule.runtime.event.impl.ProcessAfterCompletedEventImpl;
import com.itheima.bolee.framework.rule.runtime.response.ExecutionResponseImpl;
import com.itheima.bolee.framework.rule.runtime.response.FlowExecutionResponse;
import com.itheima.bolee.framework.rule.runtime.response.RuleExecutionResponse;
import com.itheima.bolee.framework.rule.runtime.rete.*;

import java.io.IOException;
import java.util.*;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class KnowledgeSessionImpl implements KnowledgeSession{
	private Context context;
	private EvaluationContextImpl evaluationContext;
	private FlowContextImpl flowContext;
	private Agenda agenda;
	private List<MessageItem> debugMessageItems=new ArrayList<MessageItem>();
	private Map<String,Object> initParameters=new HashMap<String,Object>();
	private List<Object> facts=new ArrayList<Object>();
	private List<Object> historyFacts=new ArrayList<Object>();
	private List<KnowledgePackage> knowledgePackageList=new ArrayList<KnowledgePackage>();
	private List<ReteInstance> reteInstanceList=new ArrayList<ReteInstance>();
	private Map<String,Object> parameterMap=new HashMap<String,Object>();
	private List<Map<?,?>> factMaps=new ArrayList<Map<?,?>>();
	private List<KnowledgeEventListener> eventListeners=new ArrayList<KnowledgeEventListener>();
	public KnowledgeSessionImpl(KnowledgePackage knowledgePackage) {
		this(new KnowledgePackage[]{knowledgePackage},null);
	}
	public KnowledgeSessionImpl(KnowledgePackage knowledgePackage,List<MessageItem> debugMessageItems) {
		this(new KnowledgePackage[]{knowledgePackage},debugMessageItems);
	}
	public KnowledgeSessionImpl(KnowledgePackage[] knowledgePackages,List<MessageItem> debugMessageItems){
		if(debugMessageItems!=null){
			this.debugMessageItems=debugMessageItems;			
		}
		for(KnowledgePackage knowledgePackage:knowledgePackages){
			knowledgePackageList.add(knowledgePackage);
			reteInstanceList.add(knowledgePackage.newReteInstance());
			Map<String,String> p=knowledgePackage.getParameters();
			if(p!=null){
				for(String key:p.keySet()){
					Datatype type=Datatype.valueOf(p.get(key));
					if(type.equals(Datatype.Integer)){
						initParameters.put(key, 0);
					}else if(type.equals(Datatype.Long)){
						initParameters.put(key, 0);
					}else if(type.equals(Datatype.Double)){
						initParameters.put(key, 0);
					}else if(type.equals(Datatype.Float)){
						initParameters.put(key, 0);
					}else if(type.equals(Datatype.Boolean)){
						initParameters.put(key, false);
					}else if(type.equals(Datatype.List)){
						initParameters.put(key, new ArrayList<Object>());
					}else if(type.equals(Datatype.Set)){
						initParameters.put(key, new HashSet<Object>());
					}
				}
			}
		}
		initContext();
		this.agenda=new Agenda(this,context);
	}
	
	public RuleExecutionResponse fireRules() {
		return execute(null,null,Integer.MAX_VALUE);
	}
	public RuleExecutionResponse fireRules(int max) {
		return execute(null,null,max);
	}
	public RuleExecutionResponse fireRules(AgendaFilter filter) {
		return execute(filter,null,Integer.MAX_VALUE);
	}
	public RuleExecutionResponse fireRules(AgendaFilter filter, int max) {
		return execute(filter,null,max);
	}
	public RuleExecutionResponse fireRules(Map<String, Object> parameters) {
		return execute(null,parameters,Integer.MAX_VALUE);
	}
	public RuleExecutionResponse fireRules(Map<String, Object> parameters,AgendaFilter filter) {
		return execute(filter,parameters,Integer.MAX_VALUE);
	}
	public RuleExecutionResponse fireRules(Map<String, Object> parameters,AgendaFilter filter, int max) {
		return execute(filter,parameters,max);
	}
	public RuleExecutionResponse fireRules(Map<String, Object> parameters, int max) {
		return execute(null,parameters,max);
	}
	@Override
	public FlowExecutionResponse startProcess(String processId) {
		return startProcess(processId,null);
	}
	@Override
	public FlowExecutionResponse startProcess(String processId,Map<String, Object> parameters) {
		FlowDefinition targetFlow=null;
		for(KnowledgePackage knowledgePackage:knowledgePackageList){
			Map<String, FlowDefinition> flowMap=knowledgePackage.getFlowMap();
			if(flowMap==null){
				continue;
			}
			if(flowMap.containsKey(processId)){
				targetFlow=flowMap.get(processId);
				break;
			}
		}
		if(targetFlow==null){
			throw new RuleException("Rule flow ["+processId+"] not exist.");
		}
		this.parameterMap.clear();
		this.clearInitParameters();
		this.parameterMap.putAll(initParameters);
		if(parameters!=null){
			this.parameterMap.putAll(parameters);
		}
		flowContext.setVariableMap(this.parameterMap);
		flowContext.setResponse(new ExecutionResponseImpl());
		long start=System.currentTimeMillis();
		ProcessInstance pi=targetFlow.newInstance(flowContext);
		fireEvent(new ProcessAfterCompletedEventImpl(pi,this));
		historyFacts.addAll(facts);
		facts.clear();
		ExecutionResponseImpl response=(ExecutionResponseImpl)flowContext.getResponse();
		response.setDuration(System.currentTimeMillis()-start);
		reset();
		return response;
	}
	
	private RuleExecutionResponse execute(AgendaFilter filter, Map<String, Object> params,int max){
		this.parameterMap.clear();
		this.clearInitParameters();
		this.parameterMap.putAll(initParameters);
		for(Map<?,?> map:factMaps){
			for(Object key:map.keySet()){
				this.parameterMap.put(key.toString(), map.get(key));
			}
		}
		if(params!=null){
			this.parameterMap.putAll(params);
		}
		if(!facts.contains(parameterMap)){
			facts.add(parameterMap);
		}
		long start=System.currentTimeMillis();
		for(Object fact:facts){
			evaluationRete(fact);
		}
		buildElseRules(true);
		ExecutionResponseImpl resp=(ExecutionResponseImpl)agenda.execute(filter,max);
		resp.setDuration(System.currentTimeMillis()-start);
		reset();
		return resp;
	}
	
	
	@SuppressWarnings("rawtypes")
	private void clearInitParameters(){
		for(Object obj:initParameters.values()){
			if(obj==null){
				continue;
			}
			if(obj instanceof List){
				((List)obj).clear();
			}
			if(obj instanceof Set){
				((Set)obj).clear();
			}
		}
	}
	
	private void buildElseRules(boolean buildNoLhsRules) {
		List<FactTracker> trackers=new ArrayList<FactTracker>();
		for(KnowledgePackage knowledgePackage:knowledgePackageList){
			if(buildNoLhsRules){
				List<Rule> noLhsRules=knowledgePackage.getNoLhsRules();
				if(noLhsRules!=null){
					for(Rule rule:noLhsRules){
						FactTracker tracker=new FactTracker();
						tracker.setActivation(new ActivationImpl(rule,null));
						trackers.add(tracker);
					}
				}				
			}
			buildWithElseRules(trackers, knowledgePackage);
		}
		if(trackers.size()>0){
			agenda.addTrackers(trackers);
		}
	}
	private void buildWithElseRules(List<FactTracker> trackers, KnowledgePackage knowledgePackage) {
		List<Rule> withElseRules=knowledgePackage.getWithElseRules();
		if(withElseRules==null)return;
		for(Rule rule:withElseRules){
			boolean active=false;
			for(RuleBox box:agenda.getRuleBoxes()){
				if(box.getRules().contains(rule)){
					active=true;
					break;
				}
				if(active)break;
			}
			if(active)continue;
			Rule elseRule=((KnowledgePackageImpl)knowledgePackage).getElseRule(rule);
			FactTracker tracker=new FactTracker();
			tracker.setActivation(new ActivationImpl(elseRule,null));
			trackers.add(tracker);
		}
	}
	
	
	@Override
	public Object getParameter(String key) {
		return parameterMap.get(key);
	}
	@Override
	public boolean update(Object obj) {
		reevaluate(obj);
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean insert(Object fact){
		if(!(fact instanceof GeneralEntity) && (fact instanceof Map)){
			Map<?,?> map=(Map)fact;
			factMaps.add(map);
		}else if(!facts.contains(fact)){
			return facts.add(fact);
		}
		return false;
	}
	@Override
	public boolean retract(Object fact) {
		agenda.retract(fact);
		facts.remove(fact);
		historyFacts.remove(fact);
		return true;
	}
	
	@Override
	public void assertFact(Object fact) {
		facts.add(fact);
		reevaluate(fact);
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameterMap;
	}
	
	@Override
	public List<Object> getHistoryFacts() {
		return historyFacts;
	}
	
	private void reset(){
		historyFacts.clear();
		agenda.clean();
		factMaps.clear();
		facts.clear();
	}
	
	private void reevaluate(Object obj){
		for(ReteInstance reteInstance:reteInstanceList){
			reteInstance.resetForReevaluate(obj);
		}
		agenda.reevaluate(obj, evaluationContext);
		evaluationRete(obj);
		buildElseRules(false);
	}
	
	private void evaluationRete(Object fact) {
		for(ReteInstance reteInstance:reteInstanceList){
			Collection<FactTracker> trackers=reteInstance.enter(evaluationContext, fact);
			if(trackers!=null){
				agenda.addTrackers(trackers);
			}			
		}
	}
	@Override
	public void writeLogFile() throws IOException{
		if(debugMessageItems.size()==0){
			return;
		}
		for(DebugWriter writer:Utils.getDebugWriters()){
			writer.write(debugMessageItems);
		}
		debugMessageItems.clear();
	}
	
	public List<Object> getAllFacts() {
		return facts;
	}
	
	@Override
	public void addEventListener(KnowledgeEventListener listener) {
		eventListeners.add(listener);
	}
	@Override
	public List<KnowledgeEventListener> getKnowledgeEventListeners() {
		return eventListeners;
	}
	@Override
	public boolean removeEventListener(KnowledgeEventListener listener) {
		return eventListeners.remove(listener);
	}
	
	@Override
	public void fireEvent(KnowledgeEvent event) {
		if(event instanceof ActivationEvent){
			for(KnowledgeEventListener listener:eventListeners){
				if(!(listener instanceof AgendaEventListener)){
					continue;
				}
				AgendaEventListener lis=(AgendaEventListener)listener;
				if(event instanceof ActivationCancelledEvent){
					ActivationCancelledEvent e=(ActivationCancelledEvent)event;
					lis.activationCancelled(e);
				}else if(event instanceof ActivationCreatedEvent){
					ActivationCreatedEvent e=(ActivationCreatedEvent)event;
					lis.activationCreated(e);
				}else if(event instanceof ActivationBeforeFiredEvent){
					ActivationBeforeFiredEvent e=(ActivationBeforeFiredEvent)event;
					lis.beforeActivationFired(e);
				}else if(event instanceof ActivationAfterFiredEvent){
					ActivationAfterFiredEvent e=(ActivationAfterFiredEvent)event;
					lis.afterActivationFired(e);
				}
			}
		}else if(event instanceof ProcessEvent){
			for(KnowledgeEventListener listener:eventListeners){
				if(!(listener instanceof ProcessEventListener)){
					continue;
				}
				ProcessEventListener lis=(ProcessEventListener)listener;
				if(event instanceof ProcessAfterCompletedEvent){
					ProcessAfterCompletedEvent e=(ProcessAfterCompletedEvent)event;
					lis.afterProcessCompleted(e);
				}else if(event instanceof ProcessAfterStartedEvent){
					ProcessAfterStartedEvent e=(ProcessAfterStartedEvent)event;
					lis.afterProcessStarted(e);
				}else if(event instanceof ProcessBeforeCompletedEvent){
					ProcessBeforeCompletedEvent e=(ProcessBeforeCompletedEvent)event;
					lis.beforeProcessCompleted(e);
				}else if(event instanceof ProcessBeforeStartedEvent){
					ProcessBeforeStartedEvent e=(ProcessBeforeStartedEvent)event;
					lis.beforeProcessStarted(e);
				}else if(event instanceof ProcessAfterNodeTriggeredEvent){
					ProcessAfterNodeTriggeredEvent e=(ProcessAfterNodeTriggeredEvent)event;
					lis.afterNodeTriggered(e);
				}else if(event instanceof ProcessBeforeNodeTriggeredEvent){
					ProcessBeforeNodeTriggeredEvent e=(ProcessBeforeNodeTriggeredEvent)event;
					lis.beforeNodeTriggered(e);
				}
			}
		}
	}

	
	private void initContext() {
		Map<String,String> allVariableCateogoryMap=null;
		for(KnowledgePackage knowledgePackage:knowledgePackageList){
			if(allVariableCateogoryMap==null){
				allVariableCateogoryMap=knowledgePackage.getVariableCateogoryMap();				
			}else{
				allVariableCateogoryMap.putAll(knowledgePackage.getVariableCateogoryMap());
			}
		}
		context = new ContextImpl(this,Utils.getApplicationContext(),allVariableCateogoryMap,debugMessageItems);
		evaluationContext=new EvaluationContextImpl(this,Utils.getApplicationContext(),allVariableCateogoryMap,debugMessageItems);
		flowContext=new FlowContextImpl(this,allVariableCateogoryMap,Utils.getApplicationContext(),debugMessageItems);
	}
	
	public List<ReteInstance> getReteInstanceList() {
		return reteInstanceList;
	}
}
