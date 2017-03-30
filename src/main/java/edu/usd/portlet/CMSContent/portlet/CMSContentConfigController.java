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

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;


//import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		ArrayList<String> cleanedUri = new ArrayList<String>();
		for(String page: pageUris)
		{
			logger.info("The page: " + page);
			if (page != null)
				cleanedUri.add(page);
		}
		refData.put("pageUris",cleanedUri);

		//get display type. e.g. single, collapsing, tabbed.
		String displayType = preferences.getValue("displayType","Single");
		refData.put("displayType",displayType);

		String[] displayTypes = {"Single","Expanding","Tabbed","Verical_Tabs"};
		refData.put("displayTypes",displayTypes);

		return new ModelAndView("config",refData);
	}

	@RequestMapping(params = {"action=add"})
	public void addPage(ActionRequest request, ActionResponse response
	) throws Exception 
	{
		logger.info("attempting to add a page");
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);
		//convert to an array list.
		ArrayList<String> pageUris = new ArrayList<String>(Arrays.asList(pageUriArray));
		//add an item to that list.
		pageUris.add("blank");

		//convert back and save it as the new list of channels.
		prefs.setValues("pageUri",pageUris.toArray(pageUriArray));
		//Save.
		prefs.store();
	}

	@RequestMapping(params = {"action=update"})
	public void updatePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "channel", required = false) String channel,
		@RequestParam(value = "index", required = false) String index_str
	) throws Exception 
	{
		logger.info("attempting to set page uri #" + index_str + " to: " + channel);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);
		//convert to an array list.
		ArrayList<String> pageUris = new ArrayList<String>(Arrays.asList(pageUriArray));

		int path_index = Integer.parseInt(index_str);
		//beware the off by 1 errors!
		pageUris.set(path_index - 1,channel);
		
		prefs.setValues("pageUri",pageUris.toArray(pageUriArray));
		prefs.store();
		
		logger.error("Done setting value.");
	}

	@RequestMapping(params = {"action=remove"})
	public void updatePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "index", required = false) String index_str
	) throws Exception 
	{
		logger.info("attempting to remove page uri #" + index_str);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);
		//convert to an array list.
		LinkedList<String> pageUris = new LinkedList<String>(Arrays.asList(pageUriArray));

		int path_index = Integer.parseInt(index_str);
		//beware the off by 1 errors!
		pageUris.remove(path_index - 1);

		prefs.reset("pageUri");
		prefs.store();
		prefs.setValues("pageUri",pageUris.toArray(pageUriArray));
		prefs.store();
		
		logger.error("Done removing page.");
	}

	@RequestMapping(params = {"action=updateDisplay"})
	public void updateDisplay(ActionRequest request, ActionResponse response,
		@RequestParam(value = "disp_type", required = false) String disp_type
	) throws Exception 
	{
		logger.info("attempting to update diplay type to: " + disp_type);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		prefs.setValue("displayType",disp_type);
		prefs.store();

		logger.error("Done updating display type.");
	}
}
