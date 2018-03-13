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

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletContext;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jasig.portal.search.SearchConstants;
import org.jasig.portal.search.SearchRequest;
import org.jasig.portal.search.SearchResult;
import org.jasig.portal.search.SearchResults;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class SearchContentController implements PortletConfigAware
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	public final int PREVIEW_LENGTH = 250;
	@Autowired
	List<CMSDocumentDao> dataSources;

	@Autowired
	List<CMSLayout> layouts;
	
	protected PortletConfig portletConfig;

	public void setPortletConfig(PortletConfig portletConfig)
	{
		this.portletConfig = portletConfig;
	}

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

		//Creating the model object that will be passed to the view.
		Map<String, Object> refData = new HashMap<String, Object>();

		CMSLayout layout = this.conf.getLayout(request,"maximized");

		//Preparing a the list of page content.
		ArrayList<CMSDocument> content = layout.getContent(request,dataSources);

		for (CMSDocument doc: content)
		{
			if(doc == null)
				continue;
			for (String term: searchTerms)
			{
				int rank = getRank(doc.getContent(),searchQuery.getSearchTerms());
				//logger.info("Rank of " + doc.getTitle() + ": " + rank + " for term: " + term);

				if(rank > 0)
				//if(doc.getContent().contains(term))
				{
					final SearchResult searchResult = new SearchResult();
					searchResult.setTitle(request.getPreferences().getValue("searchResultsTitle", "${portlet.title.replace('O','4'}"));
					//searchResult.setSummary(doc.getTitle());
					searchResult.setSummary(this.getSummary(doc.getContent(),searchTerms));
					
					searchResult.getType().add("CMS Content");

					//https://dev-uportal.usd.edu/uPortal/normal/render.uP?pCt=academic-career-planning-center.ctf8

					String fname = "portletfname";
					//searchResult.setExternalUrl("https://" + request.getServerName() + "/uPortal/max/render.uP?pCt="+fname);
					//searchResult.setExternalUrl(searchResult.getExternalUrl().replace("normal","max"));
					searchResults.getSearchResult().add(searchResult);
					searchResult.setRank(rank);
					break;
				}
			}
		}
		
		response.setEvent(SearchConstants.SEARCH_RESULTS_QNAME, searchResults);

		return;
	}
	
	private int getRank(String content, String query)
	{
		int ret = 0;
		try
		{
			if (content != null)
				content = content.toLowerCase();
			else
				content = "";
			if (query != null)
				query = query.toLowerCase();
			else
				query = "";
			final String[] searchTerms = query.split(" ");
			for(String term:searchTerms)
			{
				int index = content.indexOf(term);
				int lastIndex = -1;
				while (index > lastIndex)
				{
					ret += 1;
					lastIndex = index;
					index = content.indexOf(term,index) + 1;
				}
				//the above method had been a little flaky, so I'm leaving this
				//alternative here just in case.
				//if (content.contains(term))
				//	ret += 1;
			}
		}
		catch(Exception e)
		{
			logger.info("Error getting rank: " + e);
		}
		return ret;
	}
	
	private String getSummary(String content, String[] terms)
	{
		String ret = "Error fetching preview";
		//final String[] searchTerms = query.split(" ");
		try
		{
			Document doc = Jsoup.parse(content);
			ret = "";
			for(Element child: doc.children())
			{
				ret += findTerm(child,terms[0]).text();
			}
			content = ret;
			ret = "";
			for(String term:terms)
			{
				int start = content.toLowerCase().indexOf(term.toLowerCase());
				if (start == -1)
					continue;
				if (start < PREVIEW_LENGTH)
					ret += "<p>" + content.substring(0,start) + "<b>" + term + "</b>";
				else
					ret += "<p>..." + content.substring(start-PREVIEW_LENGTH,start) + "<b>" + term + "</b>";
				if (content.length() < start+term.length()+PREVIEW_LENGTH)
					ret += content.substring(start+term.length()) + "</p>";
				else
					ret += content.substring(start+term.length(),start+term.length()+PREVIEW_LENGTH) + "...</p>";
			}
			if (ret.length() == 0)
				ret = "No preview is available.";
		}
		catch(Exception e)
		{
			ret = "Error fetching preview";
			logger.warn("Error while attempting to retrieve preview" + e);
		}
		return ret;
	}

	private Element findTerm(Element parent, String term)
	{
		Element ret = parent;
		for (Element child : parent.children())
		{
			if (child.outerHtml().toLowerCase().contains(term.toLowerCase()))
			{
				Element el = findTerm(child,term);
				if(el != null)
				{
					ret = el;
				}
				else
				{
					return parent;
				}
			}
		}
		return ret;
	}
}

