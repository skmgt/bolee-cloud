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
package com.itheima.bolee.framework.rule;

/**
 * @author Jacky.gao
 * @since 2017年8月28日
 */
public class Splash {
	public void print(){
		StringBuilder sb=new StringBuilder();
		sb.append("\n");
		sb.append("_____  __________ _____  ________ ________________ ");

		sb.append("                                                                                                                                                            \n" +
				"                                                                                                                                                            \n" +
				"BBBBBBBBBBBBBBBBB                    lllllll                                                                                    lllllll                     \n" +
				"B::::::::::::::::B                   l:::::l                                                                                    l:::::l                     \n" +
				"B::::::BBBBBB:::::B                  l:::::l                                                                                    l:::::l                     \n" +
				"BB:::::B     B:::::B                 l:::::l                                                                                    l:::::l                     \n" +
				"  B::::B     B:::::B   ooooooooooo    l::::l     eeeeeeeeeeee        eeeeeeeeeeee         rrrrr   rrrrrrrrr   uuuuuu    uuuuuu   l::::l     eeeeeeeeeeee    \n" +
				"  B::::B     B:::::B oo:::::::::::oo  l::::l   ee::::::::::::ee    ee::::::::::::ee       r::::rrr:::::::::r  u::::u    u::::u   l::::l   ee::::::::::::ee  \n" +
				"  B::::BBBBBB:::::B o:::::::::::::::o l::::l  e::::::eeeee:::::ee e::::::eeeee:::::ee     r:::::::::::::::::r u::::u    u::::u   l::::l  e::::::eeeee:::::ee\n" +
				"  B:::::::::::::BB  o:::::ooooo:::::o l::::l e::::::e     e:::::ee::::::e     e:::::e     rr::::::rrrrr::::::ru::::u    u::::u   l::::l e::::::e     e:::::e\n" +
				"  B::::BBBBBB:::::B o::::o     o::::o l::::l e:::::::eeeee::::::ee:::::::eeeee::::::e      r:::::r     r:::::ru::::u    u::::u   l::::l e:::::::eeeee::::::e\n" +
				"  B::::B     B:::::Bo::::o     o::::o l::::l e:::::::::::::::::e e:::::::::::::::::e       r:::::r     rrrrrrru::::u    u::::u   l::::l e:::::::::::::::::e \n" +
				"  B::::B     B:::::Bo::::o     o::::o l::::l e::::::eeeeeeeeeee  e::::::eeeeeeeeeee        r:::::r            u::::u    u::::u   l::::l e::::::eeeeeeeeeee  \n" +
				"  B::::B     B:::::Bo::::o     o::::o l::::l e:::::::e           e:::::::e                 r:::::r            u:::::uuuu:::::u   l::::l e:::::::e           \n" +
				"BB:::::BBBBBB::::::Bo:::::ooooo:::::ol::::::le::::::::e          e::::::::e                r:::::r            u:::::::::::::::uul::::::le::::::::e          \n" +
				"B:::::::::::::::::B o:::::::::::::::ol::::::l e::::::::eeeeeeee   e::::::::eeeeeeee        r:::::r             u:::::::::::::::ul::::::l e::::::::eeeeeeee  \n" +
				"B::::::::::::::::B   oo:::::::::::oo l::::::l  ee:::::::::::::e    ee:::::::::::::e        r:::::r              uu::::::::uu:::ul::::::l  ee:::::::::::::e  \n" +
				"BBBBBBBBBBBBBBBBB      ooooooooooo   llllllll    eeeeeeeeeeeeee      eeeeeeeeeeeeee        rrrrrrr                uuuuuuuu  uuuullllllll    eeeeeeeeeeeeee  ");
		sb.append("\n.....................................................................................................\n");
		sb.append(".  保乐保险规则引擎启动\n");
		sb.append(".  规则引擎对系统的支撑非常重要，非核心业务人员勿动规则！！！.\n");
		sb.append(".  规则引擎对系统的支撑非常重要，非核心业务人员勿动规则！！！.\n");
		sb.append(".  规则引擎对系统的支撑非常重要，非核心业务人员勿动规则！！！.\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Splash s=new Splash();
		s.print();
	}
}
