/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.usd.portlet.cmscontent.portlet.editor;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

//import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.ModelAndView;

import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.InternalDao;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSConfigDao;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * This class handles the builtin CMS Content editor.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Controller
@RequestMapping("VIEW")
public class CMSEditorController {
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired 
	private InternalDao intdbo = null;
	public void setInternalDao(InternalDao intdbo)
	{
		this.intdbo = intdbo;
	}

	@Autowired
	List<CMSDocumentDao> dataSources;

	@Autowired
	private CMSConfigDao conf = null;
	public void setConf(CMSConfigDao conf)
	{
		this.conf = conf;
	}

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request, RenderResponse response)
	{
		logger.debug("Started primary view");
		final Map<String, Object> refData = new HashMap<String, Object>();

		logger.debug("fetching available pages");
		for(CMSDocumentDao ds:dataSources)
			refData.put(ds.getDaoName(),ds.getAllDocumentsContentless());

		List<String> sources = new ArrayList<String>();
		for(CMSDocumentDao ds:dataSources)
			sources.add(ds.getDaoName());
		refData.put("sources",sources.toArray());
		
		//get any paramaters that were passed.
		refData.put("parameters",request.getParameterMap());

		return new ModelAndView("editor",refData);
	}
	
	
	@RequestMapping(params = {"action=Update"})
	public void updatePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "content", required = true) String content,
		@RequestParam(value = "doc_id", required = true) String id,
		@RequestParam(value = "doc_title", required = true) String title,
		@RequestParam(value = "doc_source", required = false) String source)
	{
		logger.info("attempting to update page to1 : " + content);
		logger.info("Title: " + title);
		logger.info("Id: " + id);
		logger.info("Doc Type: html");
		logger.info("Source: " + source);
		CMSDocument doc = new CMSDocument(title, id, source, "html", content);
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName() == source)
				dbo = ds;
		dbo.saveDocument(doc);
	}
}
