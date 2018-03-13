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

import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.InternalDao;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;

@RestController
@RequestMapping("/v1/api")
public final class CMSRestController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private CMSDocumentDao csdbo = new CommonSpotDaoImpl();
	
//	@Autowired 
//	private InternalDao intdbo = null;
//	public void setInternalDao(InternalDao intdbo)
//	{
//		this.intdbo = intdbo;
//	}
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@Autowired
	List<CMSResponder> responders;


//	@RequestMapping("test")
//	public Message test(@RequestParam(value="name", defaultValue = "Me") String name)
//	{
//		logger.debug("Recieved request for name:" + name);
//		Message msg = new Message();
//		msg.setText("testing " + name);
//		return msg;
//	}

	@RequestMapping("getPages")
	public List<CMSDocument> getPages(@RequestParam(value="source", defaultValue = "CommonSpot") String source)
	{
		logger.debug("Recieved request to get pages for: " + source);
//		List<CMSDocument> pages;
//		if(source.equals("Internal"))
//			pages = intdbo.getAllDocumentsContentless();
//		else
//			pages = csdbo.getAllDocumentsContentless();
//		return pages;

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
			doc.setTitle("" + obj.getJSONObject("doc").getString("name"));
			doc.setId("form:" + obj.getJSONObject("doc").getString("id"));
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
		@RequestParam(value = "doc_source", required = false) String source)
	{
		logger.debug("Recieved request to update doc: " + id);
		CMSDocument doc = new CMSDocument(title, id, source,"html", content);
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
		logger.debug("Recieved form response: " + form + " which will be sent to: " + replyType);
		for(CMSResponder re:responders)
			if (replyType.equals(re.getName()))
				re.respond(form);
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
		@RequestParam(value="source", defaultValue = "CommonSpot") String source,
		@RequestParam(value="id", defaultValue = "1") String id
		)
	{
		logger.debug("Recieved request to get a document from: " + source + " with path: " + id);
		DocWrapper ret = new DocWrapper();
		try
		{
			CMSDocumentDao dbo = getDbo(source);
			ret.setDoc(dbo.getDocument(id));
//			if(source.equals("Internal"))
//				ret.setDoc(intdbo.getDocument(id));
//			else if (source.equals("CommonSpot"))
//				ret.setDoc(csdbo.getDocument(id));
//			else
//			{
//				logger.debug("Request made to the empty data provider");
//			}
			logger.debug("Returning result");
		}
		catch(Exception e)
		{
			logger.info("Failed to retrieve information" + e.toString());
		}
		return ret;
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

