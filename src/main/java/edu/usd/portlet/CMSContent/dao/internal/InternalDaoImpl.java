package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.IteratorUtils;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.InternalDocumentStore;
import edu.usd.portlet.cmscontent.dao.InternalDocumentInfoRepository;

public class InternalDaoImpl implements CMSDocumentDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private InternalDocumentInfoRepository docInfoRepo;
	
	public void setDocInfoRepo( InternalDocumentInfoRepository docInfoRepo)
	{
		this.docInfoRepo = docInfoRepo;
	}
	
	@Autowired
	private InternalDocumentStore IDS = new InternalDocumentStore();
	
	public void setIDS(InternalDocumentStore ids)
	{
		InternalDocumentStore IDS;
	}
//	private InternalDocumentInfoRepository docInfoRepo = new InternaldocInfoRepository();

	public List<String> getAvailableDocuments()
	{
		logger.debug("Getting all pages");
		List<CMSDocument> pages = new ArrayList<CMSDocument>();
//		InternalPageStore hb = new InternalPageStore();
//		pages = hb.getPages();
//		CMSDocument page = new CMSDocument("Test page","/path/to/test.html");
//		pages.add(page);
//		docInfoRepo.save(page);
//		page = new CMSDocument("tp2","/path/to/test2.html");
//		pages.add(page);
//		docInfoRepo.save(page);
		try
		{
//			Iterable<?> pagesIter = docInfoRepo.findAll();
			logger.debug("pages fetched.");
//			pages = IteratorUtils.toList(pagesIter.iterator());
			logger.debug("Number of pages: " + pages.size());
		}
		catch(Exception e)
		{
			logger.debug("An error was encountered!");
			logger.debug(e);
		}
		finally
		{

		}

		
//		return new ArrayList<CMSDocument>(pages);
		ArrayList<String> rets = new ArrayList<String>();
		for(CMSDocument doc:pages)
			rets.add(doc.getTitle());
		return rets;
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		List<CMSDocument> docs;
		//InternalDocumentStore hb = new InternalDocumentStore();
		try
		{
			logger.debug("Fetching pages " + IDS);
			docs = IDS.getAll();
			logger.debug("pages fetched.");
//			docs = new ArrayList<CMSDocument>(IteratorUtils.toList(pagesIter.iterator()));
			logger.debug("Number of pages: " + docs.size());
		}
		catch(Exception e)
		{
			logger.debug("An error was encountered!");
			logger.debug(e);
			docs = new ArrayList<CMSDocument>();
		}
		finally
		{

		}
		CMSDocument doc = new CMSDocument();
		doc.setTitle("Testing?");
		docs.add(doc);
		return docs;
	}

	public CMSDocument getDocument(String Id)
	{
		CMSDocument page;
		return new CMSDocument();
	}

	public void destroy() throws Exception {
	}
}
