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

import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;
//import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
//import edu.usd.portlet.cmscontent.dao.InternalDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

/**
 * This class handles retrieves information and displays the view page.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Component
@Controller
@RequestMapping("VIEW")
public class CMSContentViewController {
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	List<CMSDocumentDao> dataSources;

	@Autowired
	List<CMSLayout> layouts;

	@Autowired
	private CMSConfigDao conf = null;
	public void setConf(CMSConfigDao conf)
	{
		this.conf = conf;
	}

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request)
	{
		logger.debug("Number of datasources: " + dataSources.size());
		for(CMSDocumentDao ds:dataSources)
			logger.debug("Data source: " + ds.getDaoName());

		//Creating the model object that will be passed to the view.
		Map<String, Object> refData = new HashMap<String, Object>();

		CMSLayout layout = this.conf.getLayout(request);
		logger.debug("Number of layouts: " + layouts.size());
		logger.debug("Layout: " + layout.getName());
		logger.debug("View: " + layout.getView());
		refData.put("properties",layout.getProperties());
		refData.put("layout",layout);


		//Get content
		refData.put("content",layout.getContent(request,dataSources));

		//Get channel ID
		refData.put("channelId","CMS" + request.getWindowID());

		//Get portlet path. Not sure if this is truely necessary. I'll probably depricate it.
		refData.put("portletPath",request.getContextPath());

		//get any paramaters that were passed.
		refData.put("parameters",request.getParameterMap());
		
		//get username.
		refData.put("username",((Map)request.getAttribute(PortletRequest.USER_INFO)).get("username"));
		
		//get user email.
		refData.put("useremail",((Map)request.getAttribute(PortletRequest.USER_INFO)).get("mail"));

		return layout.display(refData);
	}
}
