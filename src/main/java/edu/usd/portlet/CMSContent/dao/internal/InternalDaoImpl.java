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

	public List<String> getAvailableDocuments()
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
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
		try
		{
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
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
}
