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
import java.util.List;
import java.util.ArrayList;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ActionRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import edu.usd.portlet.cmscontent.dao.InternalDao;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * This class handles retrieves information and displays the config page.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Controller
@RequestMapping("CONFIG")
public class CMSContentConfigController
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	List<CMSDocumentDao> dataSources;

	@Autowired
	List<CMSLayout> layouts;


	@Autowired
	CMSConfigDao conf = null;

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request)
	{
		logger.debug("Started primary view");
		final Map<String, Object> refData = new HashMap<String, Object>();
		//final PortletPreferences preferences = request.getPreferences();

		logger.debug("fetching available pages");
		ArrayList<String> sources = new ArrayList<String>();
		for(CMSDocumentDao ds:dataSources)
			refData.put(ds.getDaoName(),ds.getAllDocumentsContentless());
//		{
//			sources.add(ds.getDaoName());
//		}
		refData.put("sources",dataSources);

		CMSLayout normal = this.conf.getLayout(request,"normal");
		List<CMSDocument> docs = normal.getSubscriptionsAsDocs();
		ArrayList<CMSDocument> content = new ArrayList<CMSDocument>();
		for(CMSDocument entry:docs)
			for(CMSDocumentDao ds:dataSources)
				if (ds.getDaoName().equals(entry.getSource()))
					content.add(ds.getDocument(entry.getId()));
		logger.info("active docs: " + content);
		refData.put("activeDocumentsNormal",content);
		String displayType = normal.getView();
		refData.put("currentView",normal);

		CMSLayout max = this.conf.getLayout(request,"maximized");
		if (max != null)
		{
			List<CMSDocument> maxUris = max.getSubscriptionsAsDocs();
			refData.put("pageUrisMaximized",maxUris);
			String maxDisplayType = max.getView();
			refData.put("maximizedCurrentView",maxDisplayType);
		}

//		ArrayList<String> displayTypesal = new ArrayList<String>();
//		for(String disp:displayTypes)
//			displayTypesal.add(disp);
		
//		String[] displayTypes = {"Single","Expanding","Tabbed","Verical Tabs","Vertical Tabs with Panel"};
		refData.put("availableViews",layouts);

		return new ModelAndView("config",refData);
	}


	@RequestMapping(params = {"action=updateDocument"})
	public void updateDocument(ActionRequest request,
		@RequestParam(value = "document", required = false) String document,
		@RequestParam(value = "source", required = false) String source,
		@RequestParam(value = "index", required = false) String index_str,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		logger.info("attempting to set page uri #" + index_str + " to: '" + document + "' from: '" + source + "' is max: " + mode);
		if(document == null)
		{
			logger.debug("Cannot set page to nothing.");
			return;
		}
		if(source.equals("None"))
		{
			logger.debug("Cannot set to no data source.");
			return;
		}
		int index = Integer.parseInt(index_str) - 1;
		CMSLayout layout = this.conf.getLayout(request,mode);
		
		CMSSubscription sub = new CMSSubscription();
		sub.setDocId(document);
		sub.setDocSource(source);
		layout.updateSubscription(sub,index);
		this.conf.setLayout(request,mode,layout);
	}

	@RequestMapping(params = {"action=updateView"})
	public void updateView(ActionRequest request,
		@RequestParam(value = "disp_type", required = false) String disp_type,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		logger.debug("setting view to " + disp_type + " for mode " + mode);
		CMSLayout layout = this.conf.getLayout(request,mode);
		layout.setView(disp_type);
		this.conf.setLayout(request,mode,layout);
	}


}
