/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */

/*
 * created by diyanliang 2010-2-24
 */
package net.duckling.ddl.service.render.dml;

import org.jdom.Element;
/**
 * Introduction Here.
 * @date 2010-3-8
 * @author 狄
 */
public abstract class AbstractParseDmlElement {
	
	public Dml2Html d2h=new Dml2Html();
	
	public abstract  void printElement(Element element,Dml2HtmlEngine dml2htmlengine);
	
	public abstract  void printAttribute(Element e,Dml2HtmlEngine dml2htmlengine);
}
