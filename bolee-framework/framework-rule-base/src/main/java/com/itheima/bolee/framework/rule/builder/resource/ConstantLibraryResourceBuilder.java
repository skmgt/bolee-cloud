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
package com.itheima.bolee.framework.rule.builder.resource;

import com.itheima.bolee.framework.rule.model.library.constant.ConstantLibrary;
import com.itheima.bolee.framework.rule.parse.deserializer.ConstantLibraryDeserializer;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2015年1月15日
 */
public class ConstantLibraryResourceBuilder implements ResourceBuilder<ConstantLibrary> {
	private ConstantLibraryDeserializer constantLibraryDeserializer;
	public ConstantLibrary build(Element root) {
		return constantLibraryDeserializer.deserialize(root);
	}
	public boolean support(Element root) {
		return constantLibraryDeserializer.support(root);
	}
	public ResourceType getType() {
		return ResourceType.ConstantLibrary;
	};
	public void setConstantLibraryDeserializer(
			ConstantLibraryDeserializer constantLibraryDeserializer) {
		this.constantLibraryDeserializer = constantLibraryDeserializer;
	}
}
