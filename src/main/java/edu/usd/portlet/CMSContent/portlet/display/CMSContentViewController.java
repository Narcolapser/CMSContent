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
package edu.usd.portlet.cmscontent.portlet.display;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;
import java.util.Iterator;

//import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

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
import edu.usd.portlet.cmscontent.dao.InternalDao;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSLayout;

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

	private CMSDocumentDao csdbo = new CommonSpotDaoImpl();

	@Autowired 
	private InternalDao intdbo = null;
	public void setInternalDao(InternalDao intdbo)
	{
		this.intdbo = intdbo;
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
		//Create the model object that will be passed.
		final Map<String, Object> refData = new HashMap<String, Object>();

		CMSLayout layout = this.conf.getLayout(request);
		List<CMSDocument> uris = layout.getSubscriptionsAsDocs();
		
		//Prepare a list for the page content.
		ArrayList<CMSDocument> content = new ArrayList<CMSDocument>();
		
		//itterate through the list of pages and get their content.
		for(CMSDocument entry:uris)
		{
			logger.info("Getting page: " + entry.getId() + " " + entry.getSource());
			if("blank".equals(entry.getId()))
			{
				//skip this, it is a blank page.
				continue;
			}
			if("Internal".equals(entry.getSource()))
			{
				//content comes from internally, use the internal source.
				content.add(this.intdbo.getDocument(entry.getId()));
			}
			else
			{
				//content comes from CommonSpot, use the CommonSpot source.
				content.add(this.csdbo.getDocument(entry.getId()));
				//This is the default for legacy reasons. 
			}
		}
		refData.put("content",content); //stow it for the view.

		//Get channel ID:
		String channelId = request.getWindowID();
		refData.put("channelId",channelId);

		//Get portlet path:
		String portletPath = request.getContextPath();
		refData.put("portletPath",portletPath);

		//get any paramaters that were passed.
		refData.put("parameters",request.getParameterMap());

		return layout.display(refData);
	}
}
