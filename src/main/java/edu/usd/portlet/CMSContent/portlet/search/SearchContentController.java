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
package edu.usd.portlet.cmscontent.portlet.search;

import java.util.Locale;
import java.util.Random;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.portal.search.SearchConstants;
import org.jasig.portal.search.SearchRequest;
import org.jasig.portal.search.SearchResult;
import org.jasig.portal.search.SearchResults;
//import org.jasig.portlet.cms.service.IStringCleaningService;
//import org.jasig.portlet.cms.service.dao.IContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.context.PortletConfigAware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;


import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;



/**
 * SearchContentController provides responses to the portal's builtin search.
 * 
 * @author Toben Archer
 * @version $Revision$
 */
@Controller
@RequestMapping("VIEW")
public class SearchContentController
{
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

	@EventMapping(SearchConstants.SEARCH_REQUEST_QNAME_STRING)
	public void searchContent(EventRequest request, EventResponse response)
	{
		final Event event = request.getEvent();
		final SearchRequest searchQuery = (SearchRequest)event.getValue();
		
		final String textContent = "";
		final String[] searchTerms = searchQuery.getSearchTerms().split(" ");

		final SearchResults searchResults = new SearchResults();
		
		Random rand = new Random();
		searchResults.setQueryId(searchQuery.getQueryId());
		searchResults.setWindowId(request.getWindowID());
		
		logger.debug("Number of datasources: " + dataSources.size());
		for(CMSDocumentDao ds:dataSources)
			logger.debug("Data source: " + ds.getDaoName());

		//Creating the model object that will be passed to the view.
		Map<String, Object> refData = new HashMap<String, Object>();

		CMSLayout layout = this.conf.getLayout(request,"maximized");

		//Preparing a the list of page content.
		ArrayList<CMSDocument> content = layout.getContent(request,dataSources);

		for (CMSDocument doc: content)
			for (String term: searchTerms)
				if(doc.getContent().contains(term))
				{
					final SearchResult searchResult = new SearchResult();
					searchResult.setTitle(request.getPreferences().getValue("searchResultsTitle", "${portlet.title}"));
					searchResult.setSummary(doc.getTitle());
					searchResult.getType().add("Portlet Content");
					//https://dev-uportal.usd.edu/uPortal/normal/render.uP?pCt=academic-career-planning-center.ctf8
					//searchResult.setExternalUrl("https://" + server + ".usd.edu/uPortal/max/render.uP?pCt="+fname);
					searchResult.setExternalUrl(searchResult.getExternalUrl().replace("normal","max"));
					searchResults.getSearchResult().add(searchResult);
					break;
				}
		
		response.setEvent(SearchConstants.SEARCH_RESULTS_QNAME, searchResults);

		return;
	}
}

