package edu.usd.portlet.cmscontent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;

/**
 * This class provides CMSContent with the ability to display forms. Forms have
 * a couple of features that simple HTML documents lack. Specifically they need
 * to be personalized to every user to allow for information such as username to
 * be embedded in it so that we know who submitted the form. But the other thing
 * that is fairly unqiue is the ability to return a document as JSON. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

@Component
public class FormDaoImpl implements CMSDocumentDao, DisposableBean
{
	@Autowired
	private SwallowingJspRenderer jspRenderer;

	@Autowired
	private InternalDocumentDao internalDocumentDao;

	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		logger.debug("Fetching document with ID of: " + Id);
		FormDoc doc;
		try
		{
			doc = new FormDoc(this.internalDocumentDao.getDocumentById(Id));
			doc.setJspRenderer(jspRenderer);
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
		return (CMSDocument)doc;
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		List<CMSDocument> docs = this.internalDocumentDao.getAllDocumentsContentLess("form");
		return docs;
	}
	
	public void saveDocument(CMSDocument val)
	{
		this.internalDocumentDao.insertDocument(val);
	}
	
	public String getDaoName()
	{
		return "InternalForms";
	}
	
	public String getDisplayName(){return "Internal Forms";}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return true;}
	
	public boolean deleteEnabled(){return true;}
	
	public String getSourceType(){return "form";}
	
	public ArrayList<JSONObject> getDocJson(CMSDocument doc)
	{
		try
		{
			JSONArray obj = new JSONArray(doc.getContent());
			ArrayList<JSONObject> jobj = new ArrayList<JSONObject>();
			for(int i = 0; i < obj.length(); i++)
				jobj.add(obj.getJSONObject(i));
			return jobj;
		}
		catch(JSONException e)
		{
			logger.error("Error loading form data: " + e);
			return null;
		}
	}
}
