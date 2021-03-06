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
		//prepare object to pass to render phase.
		final Map<String, Object> refData = new HashMap<String, Object>();

		//fetching available pages
		for(CMSDocumentDao ds:dataSources)
			refData.put(ds.getDaoName(),ds.getAllDocumentsContentless());

		//Get sources and their information.
		List<String> sources = new ArrayList<String>();
		Map<String,Boolean> saveEnabled = new HashMap<String,Boolean>();
		Map<String,Boolean> deleteEnabled = new HashMap<String,Boolean>();
		for(CMSDocumentDao ds:dataSources)
		{
			if(ds.getSourceType().equals("html"))
			{
				sources.add(ds.getDaoName());
				saveEnabled.put(ds.getDaoName(),ds.writeEnabled());
				deleteEnabled.put(ds.getDaoName(),ds.writeEnabled());
			}
		}
		refData.put("sources",sources.toArray());
		refData.put("saveEnabled",saveEnabled);
		refData.put("deleteEnabled",deleteEnabled);
		
		//get any paramaters that were passed.
		refData.put("parameters",request.getParameterMap());

		return new ModelAndView("editors/document",refData);
	}
}
