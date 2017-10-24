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
public class InternalDaoImpl implements InternalDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private InternalDocumentDao internalDocumentDao;

	public InternalDocumentDao getInternalDocumentDao() {
		return internalDocumentDao;
	}

	public void setInternalDocumentDao(InternalDocumentDao internalDocumentDao) {
		logger.info("Setting Internal Document Autowire");
		this.internalDocumentDao = internalDocumentDao;
	}

	public List<String> getAvailableDocuments()
	{
		List<String> ret = new ArrayList<String>();
		try
		{
			//logger.debug("Fetching from interal CMS" + this.internalDocumentDao);
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
			//logger.debug("Fetched from internal CMS");
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
			//logger.debug("Fetching from interal CMS" + this.internalDocumentDao);
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
			//logger.debug("Fetched from internal CMS");
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
		CMSDocument page;
		try
		{
			//logger.debug("Fetching from interal CMS" + this.internalDocumentDao);
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
			//logger.debug("Fetched from internal CMS");
			page = this.internalDocumentDao.getDocumentById(Id);
		}
		catch (Exception e)
		{
			logger.debug("Error in fetching docs: " + e);
			return null;
		}
		return page;
	}
	
	public void saveDocument(CMSDocument val)
	{
		this.internalDocumentDao.insertDocument(val);
	}

	public String getDaoName()
	{
		return "Internal";
	}

	public void destroy() throws Exception {
	}
}
