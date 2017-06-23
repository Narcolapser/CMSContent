package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return new ArrayList<String>();
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		try
		{
			logger.debug("Fetching from interal CMS" + this.internalDocumentDao);
			List<CMSDocument> docs = this.internalDocumentDao.getAllDocuments();
			logger.debug("Fetched from internal CMS");
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
		return new CMSDocument();
	}

	public void destroy() throws Exception {
	}
}

//		CMSDocument doc = new CMSDocument();
//		doc.setTitle("Testing?");
//		docs.add(doc);

