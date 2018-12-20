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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;
/**
 * This class provides a REST API for the documents accessed by this portlet.
 * There are 5 principle methods:
 *   get     : Returns a requested document from specified source.
 *   list    : Lists available documents from specified source.
 *   save    : Saves a document to specified source.
 *   delete  : Deletes a document from specified source.
 *   sources : Lists available document sources.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */
@RestController
@RequestMapping("/v2/documents")
public final class DocumentApi {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@Autowired
	List<CMSResponder> responders;

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}

	@RequestMapping(value="/{source}/{id}", method=RequestMethod.GET)
	public String getDocument(
		@PathVariable(value="source") String source,
		@PathVariable(value="id") String id
		)
	{
		logger.debug("Recieved request to get a document from: " + source + " with path: " + id);
		CMSDocument val;
		try
		{
			CMSDocumentDao dbo = getDbo(source);
			val = dbo.getDocument(id);
			logger.debug(val.toString());
			return val.toJSON();
		}
		catch(Exception e)
		{
			logger.info("Failed to retrieve information" + e.toString());
			return "{}";
		}
	}
	
	@RequestMapping(value="/{source}", method=RequestMethod.GET)
	public List<CMSDocument> listDocuments(@PathVariable(value="source") String source)
	{
		logger.debug("Recieved request to list documents for: " + source);

		List<CMSDocument> docs = null;
		CMSDocumentDao dbo = getDbo(source);
		docs = dbo.getAllDocumentsContentless();
		logger.debug("Number of docs: " + docs.size());
		return docs;
	}
	
	
	@RequestMapping(value="/{source}/{id}", method=RequestMethod.POST)
	public String saveDocument(
		@PathVariable(value="source") String source,
		@PathVariable(value="id") String id,
		@RequestParam(value="document", defaultValue = "") String json
		)
	{
		logger.debug("Recieved request to update document: " + json);
		try
		{
			CMSDocument doc = new CMSDocument(json);
			doc.setId(id);
			CMSDocumentDao dbo = getDbo(source);
			logger.debug(doc);
			dbo.saveDocument(doc);
			logger.debug("Save succesful");
			return "{\"result\":\"success\"}";
		}
		catch(Exception e)
		{
			logger.error("Error saving document: " + json);
			return "{\"result\":\"failure\"}";
		}
	}


	@RequestMapping(value="/{source}/{id}", method=RequestMethod.DELETE)
	public String deleteDoc(
		@PathVariable(value="source") String source,
		@PathVariable(value="id") String id
		)
	{
		logger.debug("Recieved delete request for: " + id + " from: " + source);
		CMSDocumentDao dbo = getDbo(source);
		logger.debug("Deleting from DBO: " + dbo.getDaoName());
		if (dbo.deleteEnabled())
			dbo.deleteDocument(id);

		return "{\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/sources")
	public String getSources()
	{
		String ret = "[";
		for (CMSDocumentDao dbo:dataSources)
			ret += sourceAsJson(dbo) + ",";
		
		//remove the trailing comma and return with the closing bracket.
		return ret.substring(0,ret.length()-1) + "]";
	}
	
	private String sourceAsJson(CMSDocumentDao val)
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("name",val.getDaoName());
			obj.put("save",val.saveEnabled());
			obj.put("delete",val.deleteEnabled());
			obj.put("type",val.getSourceType());
			return obj.toString();
		}
		catch(JSONException e)
		{
			return "{}";
		}
	}
}

