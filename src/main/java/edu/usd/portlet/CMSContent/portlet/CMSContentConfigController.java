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
package edu.usd.portlet.cmscontent.portlet;

import java.util.*;

import javax.portlet.PortletPreferences;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

//import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDataDao;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * This class handles retrieves information and displays the view page.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Controller
@RequestMapping("CONFIG")
public class CMSContentConfigController
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	private CMSDataDao dbo = null; // Spring managed

	@Autowired
	public void setdbo(CMSDataDao dbo) {
		this.dbo = dbo;
	}

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request, RenderResponse response) {
		final Map<String, Object> refData = new HashMap<String, Object>();
		final PortletPreferences preferences = request.getPreferences();

		ArrayList<CMSPageInfo> pages = dbo.getAvailablePages();

		refData.put("availablePages",pages);

		String[] pageUris = preferences.getValues("pageUri",null);
		refData.put("pageUris",pageUris);

		return new ModelAndView("config",refData);
	}

	@RequestMapping(params = {"action=add"})
	public void addPage(ActionRequest request, ActionResponse response) throws Exception 
	{
		PortletPreferences prefs = request.getPreferences();
		
//		String[] pageUriArray = preferences.getValues("pageUri",null);
//		ArrayList<String> pageUris = new ArrayList<String>(pageUriArray);
		logger.error("Not actually an error, just saying that I'm now going to set the value.");
		prefs.setValue("pageUri","did it work");
		prefs.store();
		logger.error("Done setting value. should be in there now.");
	}
}
