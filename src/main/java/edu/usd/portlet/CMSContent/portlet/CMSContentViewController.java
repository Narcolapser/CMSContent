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
import java.util.Date;
import java.util.Random;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.ModelAndView;

import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDataDao;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import edu.usd.portlet.cmscontent.dao.CMSPageContent;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * This class handles retrieves information and displays the view page.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Controller
@RequestMapping("VIEW")
public class CMSContentViewController {
	protected final Log logger = LogFactory.getLog(this.getClass());

	private CMSDataDao dbo = null; // Spring managed
	@Autowired
	public void setdbo(CMSDataDao dbo) {
		this.dbo = dbo;
	}

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request, RenderResponse response)
	{
		final PortletPreferences preferences = request.getPreferences();
		//Create the model object that will be passed.
		final Map<String, Object> refData = new HashMap<String, Object>();
		//Get the page content.
		ArrayList<CMSPageContent> content = dbo.getContent(request);
		//Save the content into the model for the .jsp to display.
		refData.put("content",content);

		//get display type. e.g. single, collapsing, tabbed.
		String displayType = preferences.getValue("displayType","single");
		refData.put("displayType",displayType);

		//Get channel ID:
		Random randomGenerator = new Random();
		String channelId = "cmsContentExpanding"+String.valueOf(Math.abs(randomGenerator.nextInt()))+new Date().getTime();
		refData.put("channelId",channelId);

		//Get portlet path:
		String portletPath = request.getContextPath();
		refData.put("portletPath",portletPath);

		//send to "view".jsp the object refData.
		return new ModelAndView("view",refData);
	}
}
