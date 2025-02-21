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
package com.itheima.bolee.framework.rule.model.flow.ins;

import com.itheima.bolee.framework.rule.runtime.response.FlowExecutionResponse;
import com.itheima.bolee.framework.rule.runtime.rete.Context;

import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public interface FlowContext extends Context {
	Object getVariable(String key);
	Map<String,Object> getVariables();
	void addVariable(String key,Object object);
	void removeVariable(String key);
	List<FlowInstance> getFlowInstances();
	void addFlowInstance(FlowInstance instance);
	void setSessionValue(String key,Object value);
	Object getSessionValue(String key);
	FlowExecutionResponse getResponse();
}
