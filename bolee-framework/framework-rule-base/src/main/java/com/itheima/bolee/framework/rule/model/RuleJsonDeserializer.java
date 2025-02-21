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
package com.itheima.bolee.framework.rule.model;

import com.itheima.bolee.framework.rule.model.rule.Rule;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年10月21日
 */
public class RuleJsonDeserializer extends AbstractJsonDeserializer<List<Rule>>{
	@Override
	public List<Rule> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
        JsonNode jsonNode = oc.readTree(jp);
        Iterator<JsonNode> childrenNodesIter=jsonNode.getElements();
        List<Rule> rules=new ArrayList<Rule>();
        while(childrenNodesIter.hasNext()){
        	JsonNode childNode=childrenNodesIter.next();
        	rules.add(parseRule(jp,childNode));
        }
		return rules;
	}
}
