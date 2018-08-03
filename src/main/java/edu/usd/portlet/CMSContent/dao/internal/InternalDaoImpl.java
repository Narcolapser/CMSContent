package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.IteratorUtils;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.InternalDocumentDao;

/**
 * This class provides CMSContent basic internal storage of documents. This is
 * where I stress that CMSContent is NOT intended to be your full content
 * management service. This is meant to be a convinence utility, a sort of stop
 * gap measure. Primarily it lacks any mechanism for controlling who can publish
 * beyond the security measures of the portal or for publishing approval
 * workflows. 
 *
 * From the Java side this is a fairly straight forward class and stands as the
 * best example of a simple implementation of the CMSDocumentDao interface. 
 *
 * @author Toben Archer
 * @version $Id$
 */

@Component
@Service
public class InternalDaoImpl implements CMSDocumentDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private InternalDocumentDao internalDocumentDao;

	public InternalDocumentDao getInternalDocumentDao() {
		return internalDocumentDao;
	}

	public void setInternalDocumentDao(InternalDocumentDao internalDocumentDao)
	{
		this.internalDocumentDao = internalDocumentDao;
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		try
		{
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocumentsContentLess("html");
			return docs;
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
	}

	public CMSDocument getDocument(String Id)
	{
		CMSDocument doc;
		try
		{
			doc = this.internalDocumentDao.getDocumentById(Id);
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
		return doc;
	}
	
	public void saveDocument(CMSDocument val)
	{
		this.internalDocumentDao.insertDocument(val);
	}

	public String getDaoName()
	{
		return "Internal";
	}
	
	public String getDisplayName(){return "Internal Documents";}
	
	public boolean deleteDocument(String Id)
	{
		logger.info("Preparing to delete: " + Id);
		this.internalDocumentDao.deleteDocument(Id);
		return true;
	}

	public void destroy() throws Exception {
	}
	
	public boolean saveEnabled(){return true;}
	
	public boolean deleteEnabled(){return true;}
	
	public String getSourceType(){return "html";}
}
