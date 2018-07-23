package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.InternalDao;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;

/**
 * This class provides the original cobbled together API of this portlet. It's
 * all kludged together, don't use it. Use the DocumentAPI or the FormAPI.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@RestController
@RequestMapping("/v1/api")
public final class CMSRestController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@Autowired
	List<CMSResponder> responders;

	@RequestMapping("getPages")
	public List<CMSDocument> getPages(@RequestParam(value="source", defaultValue = "CommonSpot") String source)
	{
		logger.debug("Recieved request to get pages for: " + source);

		List<CMSDocument> pages = null;
		for(CMSDocumentDao ds:dataSources)
		{
			if (ds.getDaoName().equals(source))
			{
				pages = ds.getAllDocumentsContentless();
				logger.debug("Found datasource: " + ds.getDaoName());
				logger.debug("Number of pages: " + pages.size());
			}
		}
		return pages;
	}

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}

	@RequestMapping("delete")
	public String deleteDoc(
		@RequestParam(value="sanitybit", defaultValue = "") String sanityBit,
		@RequestParam(value="id", defaultValue = "") String id
		)
	{
		logger.debug("Recieved delete request for: " + id + " sanity bit: " + sanityBit);
		if (sanityBit.equals("31415"))
		{
			CMSDocumentDao dbo = getDbo("Internal");
			logger.debug("Deleting from DBO: " + dbo.getDaoName());
			if (dbo.deleteEnabled())
				dbo.deleteDocument(id);
		}
		else
		{
			logger.warn("Sanity check failed, bit value: " + sanityBit);
		}
		logger.debug("Home star runner ate a plate.");
		return "{\"result\":\"success\"}";
	}

	@RequestMapping("saveForm")
	public String saveForm(
		@RequestParam(value="form", defaultValue = "") String form
		)
	{
		logger.debug("Recieved request to update form: " + form);
		try
		{
			JSONObject obj = new JSONObject(form);
			CMSDocument doc = new CMSDocument();
			doc.setTitle("" + obj.getJSONObject("doc").getString("title"));
			doc.setId("" + obj.getJSONObject("doc").getString("id"));
			doc.setSource("Internal");
			doc.setDocType("form");
			doc.setContent("" + obj.getJSONArray("form"));
			logger.debug(doc);
			CMSDocumentDao dbo = getDbo("Internal");
			dbo.saveDocument(doc);
		}
		catch(JSONException e)
		{
			logger.error("Failure to decode json: " + form + " error: " + e);
		}
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value = "saveDoc", method = RequestMethod.POST)
	public String saveForm(
		@RequestParam(value = "content", required = true) String content,
		@RequestParam(value = "doc_id", required = true) String id,
		@RequestParam(value = "doc_title", required = true) String title,
		@RequestParam(value = "doc_source", required = false) String source,
		@RequestParam(value = "doc_search", required = false) String keyTerms)
	{
		logger.debug("Recieved request to update doc: " + id);
		CMSDocument doc = new CMSDocument(title, id, source, "html", content, keyTerms, false);
		CMSDocumentDao dbo = getDbo(source);
		dbo.saveDocument(doc);
		return "{\"result\":\"success\"}";
	}



	@RequestMapping("formResponse")
	public String formResponse(
		@RequestParam(value="form", defaultValue = "") String form,
		@RequestParam(value="replyType", defaultValue = "") String replyType
		)
	{
		try
		{
			logger.debug("Recieved form response: " + form + " which will be sent to: " + replyType);
			FormDaoImpl dbo = (FormDaoImpl)getDbo("InternalForms");
			JSONObject obj = new JSONObject(form);
			logger.debug(obj.getString("formId"));
			CMSDocument doc = dbo.getDocument(obj.getString("formId"));
			ArrayList<JSONObject> jform = dbo.getDocJson(doc);
			String options = "";
			for(JSONObject entry: jform)
				if(entry.getString("type").equals("respType"))
					options = entry.getString("options");
			logger.debug("Responder options: " + options);
			for(CMSResponder re:responders)
				if (replyType.equals(re.getName()))
					if(!re.respond(form,options))
					{
						logger.error("Something went wrong when submitting form: " + form);
						return "{\"result\":\"failure\"}";
					}
		}
		catch(JSONException e)
		{
			logger.error("Failure to decode json: " + form + " error: " + e);
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";
	}

	@RequestMapping("getPagesWithIndex")
	public PagesAndIndex getPagesWithIndex(
		@RequestParam(value="source", defaultValue = "CommonSpot") String source,
		@RequestParam(value="index", defaultValue = "1") String index_str
		)
	{
		logger.debug("Recieved request to get pages for: " + source + " with index: " + index_str);
		//int index = Integer.parseInt(index_str);
		PagesAndIndex ret;
		try
		{
			List<CMSDocument> pages = null;
			for(CMSDocumentDao ds:dataSources)
			{
				if (ds.getDaoName().equals(source))
				{
					pages = ds.getAllDocumentsContentless();
					logger.debug("Found datasource: " + ds.getDaoName());
					logger.debug("Number of pages: " + pages.size());
				}
			}
			if (pages == null)
			{
				logger.debug("Request made to the empty data provider");
				pages = new ArrayList<CMSDocument>();
			}
			ret = new PagesAndIndex(pages,index_str);
			logger.debug("Returning result");
		}
		catch(Exception e)
		{
			logger.error("Failed to retrieve information" + e.toString());
			ret = new PagesAndIndex();
		}
		return ret;
	}

	@RequestMapping("getDocument")
	public DocWrapper getDocument(
		@RequestParam(value="source", defaultValue = "Internal") String source,
		@RequestParam(value="id", defaultValue = "") String id
		)
	{
		if (source.equals("CommonSpot") || source.equals("CSPortalPage"))
		{
			if (id.charAt(0) != '/')
				id = "/" + id;
			if (!(id.substring(id.length()-4).equals(".cfm")))
				id = id + ".cfm";
		}
		logger.debug("Recieved request to get a document from: " + source + " with path: " + id);
		DocWrapper ret = new DocWrapper();
		try
		{
			CMSDocumentDao dbo = getDbo(source);
			CMSDocument val = dbo.getDocument(id);
			logger.debug(val.toString());
			ret.setDoc(val);
		}
		catch(Exception e)
		{
			logger.info("Failed to retrieve information" + e.toString());
		}
		return ret;
	}
	
	@RequestMapping("getResponder")
	public ResponderWrapper getResponder(@RequestParam(value="name") String name)
	{
		for (CMSResponder resp:responders)
			if(resp.getName().equals(name))
				return new ResponderWrapper(resp);
		return new ResponderWrapper();
	}

	@XmlRootElement
	public final static class ResponderWrapper
	{
		@XmlElement
		CMSResponder responder;
		
		public ResponderWrapper(){}
		public ResponderWrapper(CMSResponder resp){this.responder = resp;}
		
		public CMSResponder getResponder() {return responder;}
		
		public void setResponder(CMSResponder resp) {this.responder = resp;}
	}

	@XmlRootElement
	public final static class DocWrapper {
		@XmlElement
		CMSDocument doc;

		public CMSDocument getDoc() {return doc;}

		public void setDoc(CMSDocument doc) { this.doc = doc; }
	}

	@XmlRootElement
	public final static class PagesAndIndex
	{
		@XmlElement
		List<CMSDocument> pages;
		@XmlElement
		String index;

		public PagesAndIndex(){}

		public PagesAndIndex(List<CMSDocument> pages, String index)
		{
			this.pages = pages;
			this.index = index;
		}
		public List<CMSDocument> getPages()
		{
			return this.pages;
		}
		public void setPages(List<CMSDocument> val)
		{
			this.pages = val;
		}
		public String getIndex()
		{
			return this.index;
		}
		public void setIndex(String val)
		{
			this.index = val;
		}
	}
}

