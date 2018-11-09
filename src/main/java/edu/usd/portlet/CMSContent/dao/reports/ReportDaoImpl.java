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

import edu.usd.portlet.cmscontent.dao.DatabaseRepo;
import edu.usd.portlet.cmscontent.dao.DatabaseResponse;
import edu.usd.portlet.cmscontent.dao.DatabaseAnswer;

/**
 * This is an implementation of the CMSDocumentDao. It will display the content
 * of form responses in various ways.
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

	@Autowired
	private DatabaseRepo databaseRepo;
	
	@Autowired
	private TokenRepo tokenRepo;

	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		logger.debug("Fetching document with ID of: " + Id);
		ReportDoc doc;
		try
		{
			doc = new ReportDoc(this.internalDocumentDao.getDocumentById(Id));
			doc.setToken(tokenRepo.getToken(Id));
			doc.setJspRenderer(jspRenderer);
			doc.setFields(new FormDoc(this.internalDocumentDao.getDocumentById(Id)).getFields());
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
		//Reports are made by the response of forms. So reports IDs are the same as forms.
		List<CMSDocument> docs = this.internalDocumentDao.getAllDocumentsContentLess("form");
		return docs;
	}
	
	public void saveDocument(CMSDocument val){}
	
	public String getDaoName()
	{
		return "InternalReports";
	}

	public String getDisplayName(){return "Internal Reports";}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return true;}
	
	public boolean deleteEnabled(){return true;}
	
	public String getSourceType(){return "report";}
	
}
