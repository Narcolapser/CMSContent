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
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

//import javax.portlet.PortletPreferences;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

//import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.DNNDaoImpl;
import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDataDao;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import edu.usd.portlet.cmscontent.dao.CMSConfigDao;

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
	private CMSDataDao csdbo = new CommonSpotDaoImpl();
	private CMSDataDao dnndbo = new DNNDaoImpl();

	@Autowired
	public void setdbo(CMSDataDao dbo) {
		this.dbo = dbo;
	}

	@Autowired
	private CMSConfigDao conf = null;

	public void setConf(CMSConfigDao conf)
	{
		this.conf = conf;
	}

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request, RenderResponse response) {
		logger.debug("Started primary view");
		final Map<String, Object> refData = new HashMap<String, Object>();
		//final PortletPreferences preferences = request.getPreferences();

		logger.debug("fetching available pages");
		//ArrayList<CMSPageInfo> pages = dbo.getAvailablePages();
		ArrayList<CMSPageInfo> cspages = csdbo.getAvailablePages();
		ArrayList<CMSPageInfo> dnnpages = dnndbo.getAvailablePages();
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();

		logger.debug("puttin the pages");
		refData.put("CommonSpot",cspages);
		refData.put("DNN",dnnpages);
		refData.put("availablePages",pages);
		refData.put("None",pages);

		logger.debug("getting page Uris");
		List<CMSPageInfo> uris = this.conf.getPageUris(request);
		refData.put("pageUris",uris);

		String[] sources = {"CommonSpot","DNN"};//,"None"};
		refData.put("sources",sources);

		//get display type. e.g. single, collapsing, tabbed.
		logger.debug("getting display type.");
		String displayType = this.conf.getDisplayType(request);
		refData.put("displayType",displayType);

		String[] displayTypes = {"Single","Expanding","Tabbed"};//,"Verical_Tabs"};
		refData.put("displayTypes",displayTypes);

		return new ModelAndView("config",refData);
	}

	@RequestMapping(params = {"action=add"})
	public void addPage(ActionRequest request, ActionResponse response)
	{
		this.conf.addPage(request);
	}

	@RequestMapping(params = {"action=Update"})
	public void updatePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "channel", required = false) String channel,
		@RequestParam(value = "source", required = false) String source,
		@RequestParam(value = "index", required = false) String index_str)
	{
		logger.info("attempting to set page uri #" + index_str + " to: '" + channel + "' from: '" + source + "'");
		if(channel == null)
		{
			logger.debug("Cannot set page to nothing.");
			return;
		}
		if(source.equals("None"))
		{
			logger.debug("Cannot set to no data source.");
			return;
		}
		int index = Integer.parseInt(index_str);
		this.conf.updatePage(request, index, channel, source);
		//get the portlets preferences.
	}

	@RequestMapping(params = {"action=remove"})
	public void removePage(ActionRequest request, ActionResponse response,
		@RequestParam(value = "index", required = false) String index_str)
	{
		int index = Integer.parseInt(index_str);
		this.conf.removePage(request,index);
	}

	@RequestMapping(params = {"action=updateDisplay"})
	public void updateDisplay(ActionRequest request, ActionResponse response,
		@RequestParam(value = "disp_type", required = false) String disp_type)
	{
		this.conf.setDisplayType(request,disp_type);
	}
}
