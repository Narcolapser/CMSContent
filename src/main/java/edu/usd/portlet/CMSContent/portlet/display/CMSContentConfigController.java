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
import java.util.Arrays;

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

import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import edu.usd.portlet.cmscontent.dao.security.Role;

import edu.usd.portlet.cmscontent.service.PortletXMLGroupService;
import edu.usd.portlet.cmscontent.service.IGroupService;

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
	
	@Autowired
	IGroupService groupService;

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request)
	{
		//The data structure that will be passed to the rendering process.
		final Map<String, Object> refData = new HashMap<String, Object>();

		//Get all available documents to display on the configuration screen.
		ArrayList<String> sources = new ArrayList<String>();
		for(CMSDocumentDao ds:dataSources)
			refData.put(ds.getDaoName(),ds.getAllDocumentsContentless());
			
		//Pass in the available document sources.
		refData.put("sources",dataSources);

		//Get the portlet layout for the normal view. 
		CMSLayout normal = this.conf.getLayout(request,"normal");
		normal.getProperties();
		
		//Get all the subscriptions for the normal view. 
		List<CMSSubscription> content = normal.getSubscriptions();
		for(CMSSubscription sub : content)//Itterate over each sub.
			for(CMSDocumentDao ds:dataSources)//We need to itterate over the datasources to find which one the document came from. 
				try
				{
					if(ds.getDaoName().equals(sub.getDocSource()))
						sub.setDocTitle(ds.getDocument(sub.getDocId()).getTitle());// once found, we get the document and fetch it's title. 
				}
				catch (java.lang.NullPointerException e)
				{
					logger.warn("Could not fetch document \"" + sub.getDocId() + "\" because: " + e);
				}

		//Pass in the normal view information.
		final Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("normal",content);
		
		//Save the whole layout into to a map for later reference in the jsp.
		final Map<String, Object> layoutMap = new HashMap<String, Object>();
		layoutMap.put("normal",normal);

		//Get the maximized layout for this portlet.
		CMSLayout max = this.conf.getLayout(request,"maximized");
		
		//It is possible that no maximized layout is set.
		if (max != null)
		{
			content = max.getSubscriptions();
			for(CMSSubscription sub : content)
				for(CMSDocumentDao ds:dataSources)
					try
					{
						if(ds.getDaoName().equals(sub.getDocSource()))
							sub.setDocTitle(ds.getDocument(sub.getDocId()).getTitle());
					}
					catch (java.lang.NullPointerException e)
					{
						logger.warn("Could not fetch document \"" + sub.getDocId() + "\" because: " + e);
					}
			contentMap.put("maximized",content);
			String maxDisplayType = max.getView();
			layoutMap.put("maximized",max);
		}
		
		//Pass in the map of currently subscribed documents.
		refData.put("activeDocs",contentMap);
		
		//Pass in the currently active views.
		refData.put("activeViews",layoutMap);

		//pass in the available layoutes.
		refData.put("availableViews",layouts);
		
		//Get security roles:
		List<Role> allRoles = groupService.getAllRoles();
		List<String> userRoleNames = new ArrayList<String>();
		for (Role r : allRoles)
		{
			String roleName = r.getName();
			userRoleNames.add(roleName);
		}
		refData.put("securityRoles",userRoleNames);

		return new ModelAndView("editors/layout",refData);
	}


	//Quasi AJAX request. This is used to add or remove a document to a layout. 
	@RequestMapping(params = {"action=updateDocument"})
	public void updateDocument(ActionRequest request,
		@RequestParam(value = "document", required = false) String document,
		@RequestParam(value = "source", required = false) String source,
		@RequestParam(value = "index", required = false) String index_str,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		logger.info("attempting to set page uri #" + index_str + " to: '" + document + "' from: '" + source + "' is max: " + mode);
		if(mode.equals(""))
		{
			logger.warn("No information was provided on which layout was to be updated. This request will be ignored.");
			return;
		}
		CMSSubscription sub = null;
		//if the document is null, we are removing the specified index.
		if(document != null)
		{
			//create and fillout the subscription.
			sub = new CMSSubscription();
			sub.setDocId(document);
			sub.setDocSource(source);
		}
		
		//convert the index string into an integer.
		int index = Integer.parseInt(index_str) - 1;
		
		//get the layout for the mode we are trying to add/remove a document to/from.
		CMSLayout layout = this.conf.getLayout(request,mode);
		
		//update the subscription in the layout.
		layout.updateSubscription(sub,index);
		
		//save the new layout.
		this.conf.setLayout(request,mode,layout);
		logger.info("subscription updated.");
	}
	
	//Quasi AJAX request. This method is used to set security groups on a document.
	@RequestMapping(params = {"action=updateSecurity"})
	public void updateSecurity(ActionRequest request,
		@RequestParam(value = "document", required = true) String doc,
		@RequestParam(value = "groups", required = false) String json,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		//convert json list into a CSV.
		String groups = json.substring(1,json.length()-1).replace("\"","");
		logger.debug("updating the security groups of " + doc + " to " + groups);
		
		//split groups CSV into list of strings.
		String[] gval = groups.split(",");
		List<String> sgroups = Arrays.asList(gval);
		
		//get the layout and it's subscriptions. 
		CMSLayout layout = this.conf.getLayout(request,mode);
		List<CMSSubscription> subs = layout.getSubscriptions();
		
		//Iterate through the subscriptions until we find the relevent one. 
		for(CMSSubscription sub : subs)
			if(sub.getDocId().equals(doc))
				sub.setSecurityGroups(sgroups); //set the subscription's security groups.

		//Save the new subscriptions. 
		layout.setSubscriptions(subs);
		
		//Save the new layout. 
		this.conf.setLayout(request,mode,layout);
	}

	//Quasi AJAX request. This method is used to change the order of document on a page. 
	@RequestMapping(params = {"action=reorder"})
	public void updateOrder(ActionRequest request,
		@RequestParam(value = "index", required = true) String index_str,
		@RequestParam(value = "direction", required = true) String direction,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		logger.debug("Reordering, moving index: " + index_str + " " + direction + " for mode: " + mode);
		
		//Get layout and subscriptions.
		CMSLayout layout = this.conf.getLayout(request,mode);
		List<CMSSubscription> subs = layout.getSubscriptions();
		
		//parse index string into integer that we can use for reording the subscriptions.
		int index = Integer.parseInt(index_str) -1;
		
		//move the current subscription up one, and the one above down.
		if (direction.equals("up"))
		{
			//if this sub is at the top, skip, can't go higher.
			if (index <= 0)
				return;
			CMSSubscription sub = subs.get(index);
			subs.set(index,subs.get(index-1));
			subs.set(index-1,sub);
		}
		else
		{
			//if this sub is at the bottom, skip, can't go lower..
			if (index >= subs.size())
				return;
			CMSSubscription sub = subs.get(index);
			subs.set(index,subs.get(index+1));
			subs.set(index+1,sub);
		}
		
		//Save the new subscriptions.
		layout.setSubscriptions(subs);
		
		//Save the new layout.
		this.conf.setLayout(request,mode,layout);
	}

	//Quasi AJAX request. This method is used to change what layout is used. 
	@RequestMapping(params = {"action=updateView"})
	public void updateView(ActionRequest request,
		@RequestParam(value = "view_type", required = false) String disp_type,
		@RequestParam(value = "mode", required = false, defaultValue = "normal") String mode)
	{
		//get the layout
		CMSLayout layout = this.conf.getLayout(request,mode);
		
		//change the view that it is using. 
		layout.setView(disp_type);
		
		//Save the layout.
		this.conf.setLayout(request,mode,layout);
	}
	
	//Quasi AJAX request. This method is used to set properties on a layout.
	@RequestMapping(params = {"action=updateProperty"})
	public void updateProperty(ActionRequest request,
		@RequestParam(value = "property", required = true) String prop,
		@RequestParam(value = "value", required = true) String value)
	{
		//determing mode being updated.
		String mode = "";
		if (prop.indexOf("_") > 7)
			mode = "maximized";
		else
			mode = "normal";
		
		//Get the property being updated.
		prop = prop.substring(prop.indexOf("_") + 1,prop.length()).replace("_"," ");
		logger.debug("setting property " + prop + " of " +mode + " to: " + value);

		//Get the layout.
		CMSLayout layout = this.conf.getLayout(request,mode);
		
		//Set the property.
		layout.setProperty(prop,value);
		
		//Save the layout.
		this.conf.setLayout(request,mode,layout);
	}
}
