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

import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;

/**
 * In this implementation of the CMSDocumentDao the objective is to raise up the
 * content that has come in from form responses in a way that can be displayed
 * in the portal. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

@Component
public class ReportDaoImpl implements CMSDocumentDao, DisposableBean
{
	@Autowired
	private SwallowingJspRenderer jspRenderer;

	@Autowired
	private InternalDocumentDao internalDocumentDao;

	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		logger.debug("Fetching document with ID of: " + Id);
		ReportDoc doc;
		try
		{
			doc = new ReportDoc(this.internalDocumentDao.getDocumentById(Id));
			doc.setJspRenderer(jspRenderer);
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
		return (CMSDocument)doc;
	}

	public List<String> getAvailableDocuments()
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			List<CMSDocument> docs = this.internalDocumentDao.getAllReports();
			for(CMSDocument doc:docs)
				ret.add(doc.getTitle());
			return ret;
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		List<CMSDocument> docs = this.internalDocumentDao.getAllReportsContentLess();
		return docs;
	}
	
	public void saveDocument(CMSDocument val){}
	
	public String getDaoName()
	{
		return "InternalReports";
	}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return false;}
	
	public boolean deleteEnabled(){return false;}
	
	public String getSourceType(){return "report";}
	
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
			logger.error("Error loading report data: " + e);
			return null;
		}
	}
}
