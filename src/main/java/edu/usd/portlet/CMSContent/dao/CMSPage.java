/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.usd.portlet.cmscontent.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * FeedbackItem represents a single feedback submission.  The object contains 
 * the feedback itself, as well as information about the submitting user.
 * 
 * @author Jen Bourey
 */
public class CMSPage implements Serializable {
	
	protected String title;
	protected String path;
	protected String content;

	public CMSPage(){}
	
	public CMSPage(int id, String title, String path, String content)
	{
		this.title = title;
		this.path = path;
		this.content = content;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String val)
	{
		this.title = val;
	}
	public String getPath()
	{
		return this.path;
	}
	public void setPath(String val)
	{
		this.path = val;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String val)
	{
		this.content = val;
	}
	public String toString()
	{
		String ret = "Title: " + this.title;
		ret += " Path: " + this.path;
		return ret;
	}
}

