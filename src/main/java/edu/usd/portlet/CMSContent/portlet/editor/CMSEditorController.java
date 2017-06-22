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
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
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
	private CMSDocumentDao dbo = null; // Spring managed
	private CMSDocumentDao csdbo = new CommonSpotDaoImpl();

	public void setdbo(CMSDocumentDao dbo) {
		this.dbo = dbo;
	}

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
		//final PortletPreferences preferences = request.getPreferences();

		logger.debug("fetching available pages");
		//ArrayList<CMSDocument> pages = dbo.getAvailablePages();
		ArrayList<CMSDocument> cspages = csdbo.getAllDocumentsContentless();
		ArrayList<CMSDocument> pages = new ArrayList<CMSDocument>();

		logger.debug("puttin the pages");
		refData.put("CommonSpot",cspages);
		refData.put("availablePages",pages);
		refData.put("None",pages);

		logger.debug("getting page Uris");
		List<CMSDocument> uris = this.conf.getPageUris(request);
		refData.put("pageUris",uris);

		String[] sources = {"CommonSpot","DNN"};//,"None"};
		refData.put("sources",sources);

		//get display type. e.g. single, collapsing, tabbed.
		logger.debug("getting display type.");
		String displayType = this.conf.getDisplayType(request);
		refData.put("displayType",displayType);

		String[] displayTypes = {"Single","Expanding","Tabbed"};//,"Verical_Tabs"};
		refData.put("displayTypes",displayTypes);

		return new ModelAndView("editor",refData);
	}
	
	@RequestMapping(params = {"action=Update"})
	public void updatePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "content", required = false) String content)
	{
		logger.info("attempting to update page to : " + content);
		//get the portlets preferences.
	}
}
