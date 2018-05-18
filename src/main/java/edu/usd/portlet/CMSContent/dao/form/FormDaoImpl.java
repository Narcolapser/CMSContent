package edu.usd.portlet.cmscontent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;

/**
 * This is an implementation of the CMSDocumentDao. It is responsible for pulling in
 * data from our old CMS, CSPortalPage. It is realatively straight forward as all
 * the heavy lifting is done in the database. 
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

	public List<String> getAvailableDocuments()
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			List<CMSDocument> docs = this.internalDocumentDao.getAllForms();
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
		List<CMSDocument> docs = this.internalDocumentDao.getAllFormsContentLess();
		return docs;
	}
	
	public void saveDocument(CMSDocument val){}
	
	public String getDaoName()
	{
		return "InternalForms";
	}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return false;}
	
	public boolean deleteEnabled(){return false;}
	
	public String getSourceType(){return "form";}
}
