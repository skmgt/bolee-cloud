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

import com.itheima.bolee.framework.rule.model.table.Row;
import com.itheima.bolee.framework.rule.parse.Parser;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class RowParser implements Parser<Row> {
	public Row parse(Element element) {
		Row row=new Row();
		row.setHeight(Integer.valueOf(element.attributeValue("height")));
		row.setNum(Integer.valueOf(element.attributeValue("num")));
		return row;
	}
	public boolean support(String name) {
		return name.equals("row");
	}
}
