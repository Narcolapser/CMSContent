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
import java.util.regex.Pattern;
import java.util.Date;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;

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
import edu.usd.portlet.cmscontent.service.CMSSearchIndex;
import edu.usd.portlet.cmscontent.dao.DBConfigImpl;



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
	
	@Autowired
	CMSSearchIndex index;
	
	protected PortletConfig portletConfig;

	public void setPortletConfig(PortletConfig portletConfig)
	{
		this.portletConfig = portletConfig;
	}

	@Autowired
	DBConfigImpl conf;

	@EventMapping(SearchConstants.SEARCH_REQUEST_QNAME_STRING)
	public void searchContent(EventRequest request, EventResponse response)
	{
		logger.debug("Recieved search request");
		long start = System.currentTimeMillis();
		final Event event = request.getEvent();
		final SearchRequest searchQuery = (SearchRequest)event.getValue();
		
		final String textContent = "";
		final String[] searchTerms = searchQuery.getSearchTerms().split(" ");

		final SearchResults searchResults = new SearchResults();
		
		searchResults.setQueryId(searchQuery.getQueryId());
		searchResults.setWindowId(request.getWindowID());

		CMSLayout layout = this.conf.getLayout(request,"maximized");

		//Preparing a the list of page content.
		List<CMSDocument> content = layout.getSubscriptionsAsDocs();

		layout = this.conf.getLayout(request,"normal");
		content.addAll(layout.getSubscriptionsAsDocs());

		for (CMSDocument doc: content)
		{
			if(doc == null)
				continue;

			int rank = index.search(searchQuery.getSearchTerms(),doc.getSource(),doc.getId());
			if(rank > 0)
			{
				final SearchResult searchResult = new SearchResult();
				searchResult.setTitle(request.getPreferences().getValue("searchResultsTitle", "${portlet.title.replace('O','4'}"));
				searchResult.setSummary(getSummarySimple(index.doc_index.get(doc.getSource() + doc.getId()),searchTerms));

				searchResult.getType().add("CMS Content");

				String fname = request.getPreferences().getValue("fname",null);
				if(fname != null)
					searchResult.setExternalUrl("https://" + request.getServerName() + "/uPortal/max/render.uP?pCt="+fname);

				searchResult.setRank(rank);
				searchResults.getSearchResult().add(searchResult);
				long end = System.currentTimeMillis();
				logger.debug("Search completed in: " + (end - start));
				break;
			}
		}
		response.setEvent(SearchConstants.SEARCH_RESULTS_QNAME, searchResults);

		return;
	}

	private String getSummarySimple(String content, String[] terms)
	{
		if (content == null)
			return "content is null";
		String ret = "";
		//final String[] searchTerms = query.split(" ");
		try
		{
			content = Jsoup.parse(content).text();
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
		}
		catch(Exception e)
		{
			ret = "Error fetching preview";
			logger.warn("Error while attempting to retrieve preview" + e);
		}
		return ret;
	}


	//this code represents a much neater summary, but I couldn't get it fully working. I'm just
	//going to leave it for now. Maybe some day I'll revisit this and get the nicer preview up to
	//snuff. For now simple text it is.
	private String getSummary(String content, String[] terms)
	{
		if (content == null)
			return "content is null";
		String ret = "Error fetching preview";
		//final String[] searchTerms = query.split(" ");
		try
		{
			Document doc = Jsoup.parse(content);
			return findTerm(doc,terms[0]).html();
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
					String name = el.nodeName();
					if (name.equals("a") || name.equals("li"))
						return null;
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

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}
}

